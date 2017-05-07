package ca.rk.mappalinguarum.ui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * extension of JFileChooser for some custom functionalties
 * 
 * @author RK
 *
 */

public class FileSaver extends JFileChooser {

	private static final long serialVersionUID = 1L;

	private static String DEFAULT_FILENAME = "untitled";
	private static File selectedFile;

	/**
	 * constructs a FileSaver that saves files with specified extensions
	 * 
	 * @param exts one or more associated file extensions, e.g. { "jpg", "jpeg" }
	 * @param desc the description associated with the extensions
	 */
	public FileSaver(String[] exts, String desc) {
		FileFilter filter = new ExtensionFilter(exts, desc);
		
		setDialogType(SAVE_DIALOG);
		setFileFilter(filter);
		
		if ( exts[0].startsWith(".") ) {
			setSelectedFile( new File(DEFAULT_FILENAME + exts[0]) );
		}
		else {
			setSelectedFile( new File(DEFAULT_FILENAME + "." + exts[0]) );
		}
	}
	
	/**
	 * constructs a FileSaver with no file filtering
	 */
	public FileSaver() {
		setDialogType(SAVE_DIALOG);
		setSelectedFile( new File(DEFAULT_FILENAME + ".html") );
	}
	
	/**
	 * double-check before overwriting file
	 */
	@Override
	public void approveSelection() {
		selectedFile = getSelectedFile();
		if ( selectedFile.exists() ) {
			int result = JOptionPane.showConfirmDialog(this,
									"A file by that name already exists. Overwrite?",
									"Existing File",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE);
			switch (result) {
				case JOptionPane.YES_OPTION:
					super.approveSelection();
					return;
				case JOptionPane.NO_OPTION:
					cancelSelection();
					return;
			}
		}
		super.approveSelection();
	}
	
	/**
	 * implementation of FileFilter to filter file extensions
	 * 
	 * @author RK
	 * @see FileFilter
	 */
	private class ExtensionFilter extends FileFilter {

		private String[] extensions;
		private String description;

		/**
		 * constructs an ExtensionFilter with a set of extensions and associated
		 * description
		 * 
		 * @param exts one or more associated file extensions, e.g. { "jpg", "jpeg" }
		 * @param desc a description for the extensions
		 */
		public ExtensionFilter(String[] exts, String desc) {
			extensions = exts;
			description = desc;
			
			String tempExt;
			for (int i = 0; i < extensions.length; ++i) {
				tempExt = extensions[i].toLowerCase();
				if ( !tempExt.startsWith(".") ) {
					extensions[i] = "." + tempExt;
				}
				else {
					extensions[i] = tempExt;
				}
			}
		}
		
		/**
		 * @return true if file has an accepted extension, otherwise false
		 */
		@Override
		public boolean accept(File f) {
			if ( f.isDirectory() ) {
				return true;
			}
			
			String filename = f.getName();
			for (int i = 0; i < extensions.length; ++i) {
				if ( filename.endsWith(extensions[i]) ) {
					return true;
				}
			}
			return false;
		}

		@Override
		public String getDescription() {
			return description;
		}
	}
}
