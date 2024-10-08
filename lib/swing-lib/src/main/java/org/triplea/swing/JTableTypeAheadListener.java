package org.triplea.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.JTable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NonNls;

/** A KeyListener that implements row selection in a JTable by prefix-matching typed text. */
@RequiredArgsConstructor
public class JTableTypeAheadListener extends KeyAdapter {
  private static final int INPUT_RESET_TIME_MS = 500; // 0.5s

  private final JTable table;
  // Column that contains text data that should be matched.
  private final int columnIndex;

  @NonNls String inputString = "";
  private long keyPressTime;

  @Override
  public void keyPressed(KeyEvent evt) {
    char ch = evt.getKeyChar();
    if (!Character.isLetterOrDigit(ch)) {
      return;
    }

    long time = System.currentTimeMillis();
    if (time > keyPressTime + INPUT_RESET_TIME_MS) {
      inputString = "";
    }
    keyPressTime = time;
    inputString += Character.toLowerCase(ch);

    final var tableModel = table.getModel();
    final int rowCount = tableModel.getRowCount();
    final int selectedRow = table.getSelectedRow();
    for (int i = 0; i < rowCount; i++) {
      int row = (selectedRow + i) % rowCount;
      @NonNls String str = "" + tableModel.getValueAt(row, columnIndex);
      if (str.toLowerCase(Locale.ROOT).startsWith(inputString)) {
        selectRow(row);
        break;
      }
    }
  }

  private void selectRow(int modelRowIndex) {
    int viewRowIndex = table.getRowSorter().convertRowIndexToView(modelRowIndex);
    table.setRowSelectionInterval(viewRowIndex, viewRowIndex);
    table.scrollRectToVisible(table.getCellRect(viewRowIndex, 0, true));
  }
}
