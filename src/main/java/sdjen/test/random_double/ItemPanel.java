package sdjen.test.random_double;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import jxl.Sheet;

public class ItemPanel extends JPanel {
	JScrollPane scrollPane;
	JTable table;
	DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new Object[] { "" }) {
		public boolean isCellEditable(int row, int column) {
			return false;
		};
	};

	public ItemPanel() {
		setLayout(new BorderLayout());
		add(scrollPane = new JScrollPane(table = new JTable(model)), BorderLayout.CENTER);
		table.setShowHorizontalLines(false);
		setPreferredSize(new Dimension(200, 0));
		setValue(null);
	}

	public ItemPanel setValue(Item item) {
		model.setRowCount(0);
		if (null != item) {
			setBorder(BorderFactory.createTitledBorder(item.title));
			for (String i : item.items)
				model.addRow(new Object[] { i });
		} else {
			setBorder(BorderFactory.createTitledBorder("详情"));
		}
		return this;
	}
}
