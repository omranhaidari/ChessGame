package gui;

import com.google.common.primitives.Ints;
import engine.board.Move;
import engine.pieces.Piece;
import gui.Table.MoveLog;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

public class TakenPiecesPanel extends JPanel{
    private JPanel northPanel;
    private JPanel southPanel;
    private static final Color PANEL_COLOR = Color.lightGray;
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(50, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    
    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }
    
    // supprime tout du panel
    public void redo(MoveLog moveLog) {
        this.southPanel.removeAll();
        this.northPanel.removeAll();
        List<Piece> whiteTakenPieces = new ArrayList<>();
        List<Piece> blackTakenPieces = new ArrayList<>();
        for (Move move : moveLog.getMoves()) {
            if (move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceAlliance().isWhite())
                    whiteTakenPieces.add(takenPiece);
                else if (takenPiece.getPieceAlliance().isBlack())
                    blackTakenPieces.add(takenPiece);
                else
                    throw new RuntimeException("Should not reach here!");
            }
        }
        // pour le tri
        Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        Collections.sort(blackTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(Piece o1, Piece o2) {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });
        // affiche les pièces capturées
        for (Piece takenPiece : whiteTakenPieces) {
            try {
                BufferedImage image = ImageIO.read(new File("QChess/pieces/" +
                        takenPiece.getPieceAlliance().toString().substring(0, 1) +
                        "" + takenPiece.toString() + ".gif"));
                ImageIcon ic = new ImageIcon(image);
                JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        for (Piece takenPiece : blackTakenPieces) {
            try {
                BufferedImage image = ImageIO.read(new File("QChess/pieces/" +
                        takenPiece.getPieceAlliance().toString().substring(0, 1) +
                        "" + takenPiece.toString() + ".gif"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    } 
}
