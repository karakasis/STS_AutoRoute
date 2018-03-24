/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package STS;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Xrhstos
 */
public class CustomFileChooser extends JFileChooser {
  private String extension;
  
  public CustomFileChooser(String extension) {
    super();
    this.extension = extension;
    FileNameExtensionFilter fnef = new FileNameExtensionFilter(
      String.format("%1$s files (*.%1$s)", extension), extension);
    addChoosableFileFilter(fnef);
    setFileFilter(fnef);
  }

  @Override 
  public File getSelectedFile() {
    File selectedFile = super.getSelectedFile();

    if (selectedFile != null) {
      String name = selectedFile.getName();
      if (!name.contains("."))
        selectedFile = new File(selectedFile.getParentFile(), 
          name + '.' + extension);
    }

    return selectedFile;
  }

  @Override 
  public void approveSelection() {
    if (getDialogType() == SAVE_DIALOG) {
      File selectedFile = getSelectedFile();
      if ((selectedFile != null) && selectedFile.exists()) {
        int response = JOptionPane.showConfirmDialog(this,
          "The file " + selectedFile.getName() + 
          " already exists. Do you want to replace the existing file?",
          "Ovewrite file", JOptionPane.YES_NO_OPTION,
          JOptionPane.WARNING_MESSAGE);
        if (response != JOptionPane.YES_OPTION)
          return;
      }
    } else if (getDialogType() == OPEN_DIALOG){
        File selectedFile = getSelectedFile();
      if (!selectedFile.getName().endsWith("." + extension)) {
                JOptionPane.showMessageDialog(this,
                    "The file is not compatible with the application.", "Operation Aborted", JOptionPane.ERROR_MESSAGE);
                return;
      }
        
    }

    super.approveSelection();
  }
}