package sdjen.test.random_double;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class WorkPanel extends JPanel {
	JScrollPane scrollPane;
	JTextArea textArea;
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public WorkPanel() {
		setBorder(BorderFactory.createTitledBorder("结果"));
		setLayout(new BorderLayout());
		add(scrollPane = new JScrollPane(textArea = new JTextArea()), BorderLayout.CENTER);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		setPreferredSize(new Dimension(200, 0));
	}

	public void action(List<Item> items) {
		textArea.setText(format.format(new Date()));
		for (Item item : items) {
			textArea.append("\n");
			textArea.append(item.title);
			textArea.append(":");
			for (String s : item.getValue()) {
				textArea.append("\n\t");
				textArea.append(s);
			}
		}
	}
}
