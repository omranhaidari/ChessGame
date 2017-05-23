package gui;

import com.google.common.collect.Lists;
import engine.board.Board;
import engine.board.BoardUtils;
import engine.board.Move;
import engine.board.Tile;
import engine.pieces.Piece;
import engine.player.MoveTransition;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.SwingUtilities.isRightMouseButton;
import static javax.swing.SwingUtilities.isLeftMouseButton;

public class Table {
    private JFrame gameFrame;
    private BoardPanel boardPanel;
    private Board chessBoard;
    private final MoveLog moveLog;
    private GameHistoryPanel gameHistoryPanel;
    private TakenPiecesPanel takenPiecesPanel;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;
    
    private Dimension outerFrameDimension = new Dimension(1000, 800);
    private Dimension boardPanelDimension = new Dimension(500, 500);
    private Dimension tilePanelDimension = new Dimension(10, 10);
    private String defaultPieceImagesPath = "pieces/";
    private Color lightTileColor = Color.decode("#FFFACD");
    private Color darkTileColor = Color.decode("#593E1A");
    
    public Table() {
        this.gameFrame = new JFrame("QChess"); // Fenêtre du jeu
        this.gameFrame.setLayout(new BorderLayout());
        JMenuBar tableMenuBar = createTableMenuBar(); // Barre de menu
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(outerFrameDimension);
        this.chessBoard = Board.createStandardBoard();
        this.gameHistoryPanel = new GameHistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;
        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.gameHistoryPanel, BorderLayout.EAST);
        this.gameFrame.setIconImage(new ImageIcon("img/icon.png").getImage());
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }
    
    private JMenu createFileMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenuItem openFile = new JMenuItem("New File");
        openFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open up a new file!");
            }
        });
        fileMenu.add(openFile);
        
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }
    
    private JMenu createPreferencesMenu() {
        JMenu preferencesMenu = new JMenu("Preferences");
        JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener() {
            // inverse le plateau si on clique Flip Board
            @Override
            public void actionPerformed(ActionEvent e) {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();
        JCheckBoxMenuItem legalMoveHighlighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMoveHighlighterCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlightLegalMoves = legalMoveHighlighterCheckBox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckBox);
        return preferencesMenu;
    }
    
    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };
        abstract List<TilePanel> traverse(List<TilePanel> boardTiles); // inverse la liste de cases
        abstract BoardDirection opposite();
    }
    
    // sous classe de JPanel : représente le plateau de jeu
    private class BoardPanel extends JPanel {
        private List<TilePanel> boardTiles;

        public BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            // on ajoute toutes les cases au BoardPanel
            for (int i = 0 ; i < BoardUtils.numTiles ; i++) {
                TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(boardPanelDimension);
            validate();
        }
        
        public void drawBoard(Board board) {
            removeAll();
            // cherche en fonction du sens du plateau de jeu
            for (TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }
    
    // historique des déplacements
    public static class MoveLog {
        private List<Move> moves;

        public MoveLog() {
            this.moves = new ArrayList<>();
        }
        
        public List<Move> getMoves() {
            return this.moves;
        }
        
        public void addMove(Move move) {
            this.moves.add(move);
        }
        
        // retourne le nombre d'éléments dans MoveLog
        public int size() {
            return this.moves.size();
        }
        
        public void clear() {
            this.moves.clear();
        }
        
        public Move removeMove(int index) {
            return this.moves.remove(index);
        }
        
        public boolean removeMove(Move move) {
            return this.moves.remove(move);
        }
    }
    
    // sous classe de JPanel : représente les cases
    private class TilePanel extends JPanel {
        private int tileId; // id des cases : 0 à 63

        public TilePanel(BoardPanel boardPanel, int tileId) {
            super(new GridLayout());
            this.tileId = tileId;
            setPreferredSize(tilePanelDimension);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            
            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // le clique droit sur une case désélectionne la pièce
                    if(isRightMouseButton(e)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    // le clique gauche 
                    else if (isLeftMouseButton(e)) {                 
                        // premier clique pour la sélection de la pièce
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();
                            // si il n'y a pas de pièce sur la case, ça ne marche pas
                            if (humanMovedPiece == null)
                                sourceTile = null;
                        }
                        // deuxième clique pour le déplacement de la pièce
                        else {
                            destinationTile = chessBoard.getTile(tileId);
                            Move move = Move.MoveFactory.createMove(chessBoard, 
                                    sourceTile.getTileCoordinate(), 
                                    destinationTile.getTileCoordinate());
                            MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            } 
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null; 
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            gameHistoryPanel.redo(chessBoard, moveLog);
                            takenPiecesPanel.redo(moveLog);
                            boardPanel.drawBoard(chessBoard);
                        }
                    });
                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });
            validate();
        }
        
        public void drawTile(Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(chessBoard);
            validate();
            repaint();
        }
        
        // place les images des pièces sur la bonne case
        private void assignTilePieceIcon(Board board) {
            this.removeAll(); // supprime tous les éléments du JPanel
            if (board.getTile(this.tileId).isTileOccupied()) {
                try { 
                    // les images sont dans un répértoire et référencent chaque pièce
                    // concaténation de la couleur, la pièce et gif : White Rook => WR.gif
                    BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +
                            board.getTile(this.tileId).getPiece().getPieceAlliance().toString().substring(0, 1) +
                            board.getTile(this.tileId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardUtils.eighthRank[this.tileId] ||
                    BoardUtils.sixthRank[this.tileId] ||
                    BoardUtils.fourthRank[this.tileId] ||
                    BoardUtils.secondRank[this.tileId]) {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }
            else if (BoardUtils.seventhRank[this.tileId] ||
                    BoardUtils.fifthRank[this.tileId] ||
                    BoardUtils.thirdRank[this.tileId] ||
                    BoardUtils.firstRank[this.tileId]) {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
        
        // indique les mouvements possibles
        private void highlightLegals(Board board) {
            if(highlightLegalMoves) {
                for (Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("other/green_dot.png")))));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        
        private Collection<Move> pieceLegalMoves(Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.currentPlayer().getAlliance())
                return humanMovedPiece.calculateLegalMoves(board);
            return Collections.emptyList();
        }
    }
}