package gui;

import engine.board.Board;
import engine.board.BoardUtils;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Table {
    private JFrame gameFrame;
    private BoardPanel boardPanel;
    private Board chessBoard;
    private Dimension outerFrameDimension = new Dimension(600, 600);
    private Dimension boardPanelDimension = new Dimension(400, 350);
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
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private JMenuBar createTableMenuBar() {
        JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
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
        
        
    }
    
    // ous classe de JPanel : représente les cases
    private class TilePanel extends JPanel {
        private int tileId; // id des cases : 0 à 63

        public TilePanel(BoardPanel boardPanel, int tileId) {
            super(new GridLayout());
            this.tileId = tileId;
            setPreferredSize(tilePanelDimension);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            validate();
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
    }
}
