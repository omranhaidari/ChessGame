package gui;

import engine.board.Board;
import engine.board.Move;
import gui.Table.MoveLog;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GameHistoryPanel extends JPanel {
    private DataModel model;
    private JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(150, 400);

    public GameHistoryPanel() {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }
    
    public void redo(Board board, MoveLog moveHistory) {
        int currentRow = 0;
        this.model.clear();
        for (Move move : moveHistory.getMoves()) {
            String moveText = move.toString();
            if (move.getMovedPiece().getPieceAlliance().isWhite())
                this.model.setValueAt(moveText, currentRow, 0);
            else if (move.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }
        if (moveHistory.getMoves().size() > 0) {
            Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            String moveText = lastMove.toString();
            if (lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
                this.model.setValueAt(moveText +
                        calculateCheckAndCheckMateHash(board), currentRow, 0);
            }
            else if (lastMove.getMovedPiece().getPieceAlliance().isBlack()) {
                this.model.setValueAt(moveText +
                        calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }
        }
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calculateCheckAndCheckMateHash(Board board) {
        if (board.currentPlayer.isInCheckMate())
            return "#";
        else if (board.currentPlayer.isInCheck())
            return "+";
        return "";
    }
        
    
    private static class DataModel extends DefaultTableModel {
        private List<Row> values;
        private static final String[] NAMES = {"White", "Black"};

        public DataModel() {
            this.values = new ArrayList<>();
        }
        
        public void clear() {
            this.values.clear();
            setRowCount(0);
        }
        
        @Override
        public int getRowCount() {
            if (this.values == null)
                return 0;
            return this.values.size();
        }
        
        @Override
        public int getColumnCount() {
            return NAMES.length;
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            Row currentRow = this.values.get(row);
            if (column == 0)
                return currentRow.getWhiteMove();
            else if (column == 1) {
                return currentRow.getBlackMove();
            }
            return null;
        }
        @Override
        public void setValueAt(Object aValue, int row, int column) {
            Row currentRow;
            if (this.values.size() <= row){
                currentRow = new Row();
                this.values.add(currentRow);
            }
            else 
                currentRow = this.values.get(row);
            if (column == 0) {
                currentRow.setWhiteMove((String) aValue);
                fireTableRowsInserted(row, row);
            }
            else if (column == 1){
                currentRow.setBlackMove((String) aValue);
                fireTableCellUpdated(row, column);
            }
        }
        
        @Override
        public Class<?> getColumnClass(int column) {
            return Move.class;
        }
        
        @Override
        public String getColumnName(int column) {
            return NAMES[column];
        }
    }
    
    private static class Row {
        private String whiteMove;
        private String blackMove;

        public Row() {
            
        }
        
        public String getWhiteMove() {
            return this.whiteMove;
        }
        
        public String getBlackMove() {
            return this.blackMove;
        }

        public void setWhiteMove(String move) {
            this.whiteMove = move;
        }

        public void setBlackMove(String move) {
            this.blackMove = move;
        }
    }
}
