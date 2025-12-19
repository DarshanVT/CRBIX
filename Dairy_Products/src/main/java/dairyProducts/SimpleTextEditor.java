package dairyProducts;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;

public class SimpleTextEditor {
 public static void main(String[] args) {
     SwingUtilities.invokeLater(() -> {
         EditorFrame frame = new EditorFrame();
         frame.setVisible(true);
     });
 }
}

class EditorFrame extends JFrame {
 private final JTextArea textArea;
 private final JTextArea lineNumbers;
 private final JLabel statusLabel;
 private final JFileChooser fileChooser = new JFileChooser();
 private File currentFile = null;
 private final UndoManager undoManager = new UndoManager();

 public EditorFrame() {
     setTitle("SimpleTextEditor");
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setSize(900, 600);
     setLocationRelativeTo(null);
    
     textArea = new JTextArea();
     textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
     textArea.setTabSize(4);
     textArea.setLineWrap(false);
     
     lineNumbers = new JTextArea("1");
     lineNumbers.setEditable(false);
     lineNumbers.setBackground(new Color(230, 230, 230));
     lineNumbers.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
     lineNumbers.setBorder(BorderFactory.createMatteBorder(0,0,0,1,Color.LIGHT_GRAY));
     
     textArea.getDocument().addDocumentListener(new DocumentListener() {
         private void update() {
             SwingUtilities.invokeLater(() -> {
                 int lines = textArea.getLineCount();
                 StringBuilder sb = new StringBuilder();
                 for (int i = 1; i <= lines; i++) {
                     sb.append(i).append(System.lineSeparator());
                 }
                 lineNumbers.setText(sb.toString());
                 updateStatus(); 
             });
         }
         public void insertUpdate(DocumentEvent e) { update(); }
         public void removeUpdate(DocumentEvent e) { update(); }
         public void changedUpdate(DocumentEvent e) { update(); }
     });    
     textArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));    
     textArea.addCaretListener(e -> updateStatus());
    
     JScrollPane scroll = new JScrollPane(textArea);
     scroll.setRowHeaderView(lineNumbers);
     
     statusLabel = new JLabel("Ln 1, Col 1");
     JPanel statusBar = new JPanel(new BorderLayout());
     statusBar.setBorder(BorderFactory.createEmptyBorder(2,6,2,6));
     statusBar.add(statusLabel, BorderLayout.WEST);
    
     setJMenuBar(createMenuBar());
     add(scroll, BorderLayout.CENTER);
     add(statusBar, BorderLayout.SOUTH);  
     setupKeyBindings();
     
     fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
     
     updateLineNumbers();
 }
 private JMenuBar createMenuBar() {
     JMenuBar mb = new JMenuBar();     
     JMenu file = new JMenu("File");
     JMenuItem newIt = new JMenuItem("New");
     newIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK));
     newIt.addActionListener(e -> newFile());
     JMenuItem openIt = new JMenuItem("Open...");
     openIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
     openIt.addActionListener(e -> openFile());
     JMenuItem saveIt = new JMenuItem("Save");
     saveIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
     saveIt.addActionListener(e -> saveFile());
     JMenuItem saveAsIt = new JMenuItem("Save As...");
     saveAsIt.addActionListener(e -> saveFileAs());
     JMenuItem exitIt = new JMenuItem("Exit");
     exitIt.addActionListener(a -> exitApp());
     file.add(newIt); file.add(openIt); file.add(saveIt); file.add(saveAsIt); file.addSeparator(); file.add(exitIt);
    
     JMenu edit = new JMenu("Edit");
     JMenuItem undoIt = new JMenuItem("Undo");
     undoIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
     undoIt.addActionListener(e -> undo());
     JMenuItem redoIt = new JMenuItem("Redo");
     redoIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
     redoIt.addActionListener(e -> redo());
     JMenuItem cutIt = new JMenuItem("Cut");
     cutIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_DOWN_MASK));
     cutIt.addActionListener(e -> textArea.cut());
     JMenuItem copyIt = new JMenuItem("Copy");
     copyIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK));
     copyIt.addActionListener(e -> textArea.copy());
     JMenuItem pasteIt = new JMenuItem("Paste");
     pasteIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK));
     pasteIt.addActionListener(e -> textArea.paste());
     JMenuItem findIt = new JMenuItem("Find / Replace...");
     findIt.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK));
     findIt.addActionListener(e -> openFindReplaceDialog());
     edit.add(undoIt); edit.add(redoIt); edit.addSeparator(); edit.add(cutIt); edit.add(copyIt); edit.add(pasteIt); edit.addSeparator(); edit.add(findIt);
     
     JMenu view = new JMenu("View");
     JMenuItem fontInc = new JMenuItem("Increase Font");
     fontInc.addActionListener(e -> changeFontSize(2));
     JMenuItem fontDec = new JMenuItem("Decrease Font");
     fontDec.addActionListener(e -> changeFontSize(-2));
     view.add(fontInc); view.add(fontDec);
     
     JMenu help = new JMenu("Help");
     JMenuItem about = new JMenuItem("About");
     about.addActionListener(e -> JOptionPane.showMessageDialog(this,"SimpleTextEditor\nBuilt with Java Swing\n\nBasic features: New/Open/Save/Undo/Redo/Find", "About", JOptionPane.INFORMATION_MESSAGE));
     help.add(about);
     mb.add(file); mb.add(edit); mb.add(view); mb.add(help);
     return mb;
 }
 private void newFile() {
     if (confirmSaveIfNeeded()) {
         textArea.setText("");
         currentFile = null;
         setTitle("SimpleTextEditor");
         undoManager.discardAllEdits();
         updateLineNumbers();
     }
 }
 private void openFile() {
     if (!confirmSaveIfNeeded()) return;
     int res = fileChooser.showOpenDialog(this);
     if (res == JFileChooser.APPROVE_OPTION) {
         File f = fileChooser.getSelectedFile();
         try {
             String content = new String(Files.readAllBytes(f.toPath()));
             textArea.setText(content);
             currentFile = f;
             setTitle("SimpleTextEditor - " + f.getName());
             undoManager.discardAllEdits();
             updateLineNumbers();
         } catch (IOException ex) {
             showError("Unable to open file:\n" + ex.getMessage());
         }
     }
 }
 private void saveFile() {
     if (currentFile == null) {
         saveFileAs();
         return;
     }
     try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(currentFile), "UTF-8"))) {
         w.write(textArea.getText());
         setTitle("SimpleTextEditor - " + currentFile.getName());
     } catch (IOException ex) {
         showError("Unable to save file:\n" + ex.getMessage());
     }
 }
 private void saveFileAs() {
     int res = fileChooser.showSaveDialog(this);
     if (res == JFileChooser.APPROVE_OPTION) {
         File f = fileChooser.getSelectedFile();
         currentFile = f;
         saveFile();
     }
 }
 private void exitApp() {
     if (!confirmSaveIfNeeded()) return;
     dispose();
 }
 private boolean confirmSaveIfNeeded() {
     
     if (currentFile != null) {
         try {
             String onDisk = new String(Files.readAllBytes(currentFile.toPath()));
             if (!onDisk.equals(textArea.getText())) {
                 int opt = JOptionPane.showConfirmDialog(this, "Save changes to " + currentFile.getName() + "?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
                 if (opt == JOptionPane.CANCEL_OPTION) return false;
                 if (opt == JOptionPane.YES_OPTION) saveFile();
             }
         } catch (IOException ex) {
             
             int opt = JOptionPane.showConfirmDialog(this, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
             if (opt == JOptionPane.YES_OPTION) saveFile();
         }
     } else {
         if (!textArea.getText().isEmpty()) {
             int opt = JOptionPane.showConfirmDialog(this, "Save changes?", "Save", JOptionPane.YES_NO_CANCEL_OPTION);
             if (opt == JOptionPane.CANCEL_OPTION) return false;
             if (opt == JOptionPane.YES_OPTION) saveFileAs();
         }
     }
     return true;
 }

 private void showError(String msg) {
     JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
 }
 
 private void undo() {
     if (undoManager.canUndo()) undoManager.undo();
 }
 private void redo() {
     if (undoManager.canRedo()) undoManager.redo();
 }

 private void changeFontSize(int delta) {
     Font f = textArea.getFont();
     int size = Math.max(8, f.getSize() + delta);
     textArea.setFont(new Font(f.getFamily(), f.getStyle(), size));
     lineNumbers.setFont(new Font(f.getFamily(), f.getStyle(), size));
 }

 private void openFindReplaceDialog() {
     JDialog dlg = new JDialog(this, "Find / Replace", false);
     dlg.setLayout(new BorderLayout());
     JPanel p = new JPanel(new GridBagLayout());
     GridBagConstraints c = new GridBagConstraints();
     c.insets = new Insets(6,6,6,6);
     c.gridx = 0; c.gridy = 0; c.anchor = GridBagConstraints.EAST;
     p.add(new JLabel("Find:"), c);
     c.gridx = 1; c.gridy = 0; c.anchor = GridBagConstraints.WEST;
     JTextField findField = new JTextField(20);
     p.add(findField, c);
     c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.EAST;
     p.add(new JLabel("Replace:"), c);
     c.gridx = 1; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
     JTextField replaceField = new JTextField(20);
     p.add(replaceField, c);
     JCheckBox matchCase = new JCheckBox("Match case");
     c.gridx = 1; c.gridy = 2; c.anchor = GridBagConstraints.WEST;
     p.add(matchCase, c);

     JPanel buttons = new JPanel();
     JButton findNext = new JButton("Find Next");
     JButton replace = new JButton("Replace");
     JButton replaceAll = new JButton("Replace All");
     buttons.add(findNext); buttons.add(replace); buttons.add(replaceAll);
     
     final int[] lastPos = {0};
     findNext.addActionListener(a -> {
         String text = textArea.getText();
         String target = findField.getText();
         if (target.isEmpty()) return;
         String hay = matchCase.isSelected() ? text : text.toLowerCase();
         String needle = matchCase.isSelected() ? target : target.toLowerCase();
         int pos = hay.indexOf(needle, lastPos[0]);
         if (pos >= 0) {
             textArea.requestFocus();
             textArea.select(pos, pos + needle.length());
             lastPos[0] = pos + needle.length();
         } else {
             
             lastPos[0] = 0;
             JOptionPane.showMessageDialog(dlg, "No more occurrences", "Find", JOptionPane.INFORMATION_MESSAGE);
         }
     });
     replace.addActionListener(a -> {
         if (textArea.getSelectedText() != null && textArea.getSelectedText().length() > 0) {
             textArea.replaceSelection(replaceField.getText());
         } else {
             findNext.doClick();
         }
     });
     replaceAll.addActionListener(a -> {
         String text = textArea.getText();
         String target = findField.getText();
         if (target.isEmpty()) return;
         if (!matchCase.isSelected()) {
             String lower = text.toLowerCase();
             String tlow = target.toLowerCase();
             int idx = lower.indexOf(tlow);
             if (idx < 0) {
                 JOptionPane.showMessageDialog(dlg, "No occurrences", "Replace All", JOptionPane.INFORMATION_MESSAGE);
                 return;
             }
             StringBuilder sb = new StringBuilder();
             int start = 0;
             while (idx >= 0) {
                 sb.append(text, start, idx);
                 sb.append(replaceField.getText());
                 start = idx + tlow.length();
                 idx = lower.indexOf(tlow, start);
             }
             sb.append(text.substring(start));
             textArea.setText(sb.toString());
         } else {
             textArea.setText(text.replace(target, replaceField.getText()));
         }
     });

     dlg.add(p, BorderLayout.CENTER);
     dlg.add(buttons, BorderLayout.SOUTH);
     dlg.pack();
     dlg.setLocationRelativeTo(this);
     dlg.setVisible(true);
 } 
 private void setupKeyBindings() {
     InputMap im = textArea.getInputMap(JComponent.WHEN_FOCUSED);
     ActionMap am = textArea.getActionMap();

     im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK), "undo");
     am.put("undo", new AbstractAction() {
         public void actionPerformed(ActionEvent e) { undo(); }
     });

     im.put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK), "redo");
     am.put("redo", new AbstractAction() {
         public void actionPerformed(ActionEvent e) { redo(); }
     });

     im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK), "find");
     am.put("find", new AbstractAction() {
         public void actionPerformed(ActionEvent e) { openFindReplaceDialog(); }
     });
    
     im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
     am.put("save", new AbstractAction() {
         public void actionPerformed(ActionEvent e) { saveFile(); }
     });
 }
 private void updateStatus() {
     try {
         int caretPos = textArea.getCaretPosition();
         int line = textArea.getLineOfOffset(caretPos);
         int col = caretPos - textArea.getLineStartOffset(line);
         statusLabel.setText("Ln " + (line + 1) + ", Col " + (col + 1) + (currentFile != null ? "    [" + currentFile.getName() + "]" : ""));
     } catch (BadLocationException ex) {
         statusLabel.setText("Ln 1, Col 1");
     }
 }

 private void updateLineNumbers() {
     SwingUtilities.invokeLater(() -> {
         int lines = textArea.getLineCount();
         StringBuilder sb = new StringBuilder();
         for (int i = 1; i <= lines; i++) sb.append(i).append(System.lineSeparator());
         lineNumbers.setText(sb.toString());
     });
 }
}