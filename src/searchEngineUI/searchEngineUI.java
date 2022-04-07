package searchEngineUI;

import java.awt.Button;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import invertedIndex.invertedIndex;

public class searchEngineUI extends Frame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// declaring object of TextField class
	TextField webSearch, timeReq;

	// declaring object of Label class
	Label searchLabel, timeLable;

	// declaring object of TextArea class
	TextArea textArea, timeArea;

	public searchEngineUI() {
		searchLabel = new Label("Search:");
		searchLabel.setBounds(80, 50, 180, 20);

		webSearch = new TextField();
		webSearch.setBounds(150, 50, 170, 20);

		Button btn = new Button("Search");
		btn.setBounds(400, 45, 80, 30);

		textArea = new TextArea();
		textArea.setBounds(65, 95, 985, 625);

		timeLable = new Label("Time for searching:");
		timeLable.setBounds(60, 750, 170, 20);

		timeArea = new TextArea();
		timeArea.setBounds(60, 780, 200, 20);

		btn.addActionListener(this);

		add(btn);
		add(webSearch);
		add(textArea);
		add(timeLable);
		add(timeArea);
		add(searchLabel);

		setSize(1100, 835);
		setLayout(null);
		setVisible(true);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});

	}

	public void actionPerformed(ActionEvent e) {
		long commenceTime, endTime;
		String file = "";
		try {

			// setting timer start
			commenceTime = System.currentTimeMillis();

			invertedIndex invIndex = new invertedIndex();
			invIndex.processCleanedTextFiles();
			textArea.setText(file);
			timeArea.setText(file);

			List<String> files = invIndex.searchText(webSearch.getText());

			for (int i = 0; i < files.size(); i = i + 2) {
				// Print the file url and its content stored in the list
				file = "URL: " + files.get(i) + "\nFile Content: " + files.get(i + 1) + "\n\n";
				textArea.append(file);

			}

			// stopping the timer
			endTime = System.currentTimeMillis();

			long totalTime = endTime - commenceTime;
			// print time at the bottom
			timeArea.append(totalTime + " Milli Seconds");
		} catch (IOException exp) {
			System.out.println("Exception in actionPerformed method in searchEngineUI class: " + exp);
		}

	}

	public static void executeUI() {
		new searchEngineUI();
	}

}
