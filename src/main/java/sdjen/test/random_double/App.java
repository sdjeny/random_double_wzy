package sdjen.test.random_double;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import jxl.Sheet;
import jxl.Workbook;

/**
 * Hello world!
 *
 */
public class App extends JFrame {
	public static void main(String[] args) {
		System.out.println("请选择对应模板，具体设置见“复杂.xls”");
		new App();
	}

	JButton reloadbtn;
	private ItemPanel itemPanel;
	private WorkPanel workPanel;

	public App() {
		super();
		setTitle("双随机");
		try {
			setIconImage(new ImageIcon(getClass().getClassLoader().getResource("sdjen/test/random_double/random.png"))
					.getImage());
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			// UIManager.setLookAndFeel("com.sun.java.swing.plaf.mac.MacLookAndFeel");
		} catch (Exception e2) {
		}
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());
		JSplitPane splitPane = new JSplitPane();
		add(splitPane, BorderLayout.CENTER);
		splitPane.setLeftComponent(itemPanel = new ItemPanel());
		JPanel panel = new JPanel(new BorderLayout());
		splitPane.setRightComponent(panel);
		panel.add(workPanel = new WorkPanel(), BorderLayout.CENTER);
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		final DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new Object[] { "", "数量", "范围", "" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		final JTable table = new JTable(model);
		table.setRowHeight(22);
		TableColumn column = table.getColumnModel().getColumn(0);
		column.setPreferredWidth(200);
		column = table.getColumnModel().getColumn(3);
		column.setMinWidth(0);
		column.setMaxWidth(0);
		column.setWidth(0);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(new Dimension(0, 150));
		panel.add(scrollPane, BorderLayout.NORTH);
		add(btnPanel, BorderLayout.SOUTH);
		btnPanel.add(reloadbtn = new JButton("加载配置"));
		final JButton actbtn = new JButton("重新抽签");
		btnPanel.add(actbtn);
		JButton abortbtn = new JButton("退出");
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				try {
					itemPanel.setValue((Item) table.getValueAt(table.getSelectedRow(), 3));
				} catch (Exception e1) {
					itemPanel.setValue(null);
				}
			}
		});
		reloadbtn.addActionListener(new ActionListener() {
			private File f;

			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser chooser = new JFileChooser(null == f ? new File(".") : f);
					chooser.setDialogTitle(App.this.getTitle()+"-"+"选择配置");
					chooser.setFileFilter(new FileFilter() {
						@Override
						public String getDescription() {
							return "Excel表格";
						}

						@Override
						public boolean accept(File f) {
							return f.getAbsolutePath().toLowerCase().endsWith(".xls");
						}
					});
					if (JFileChooser.APPROVE_OPTION == chooser.showOpenDialog(App.this)) {
						f = chooser.getSelectedFile();
						// 创建一个工作簿
						Workbook workbook = Workbook.getWorkbook(f);
						// 获得所有工作表
						Sheet[] sheets = workbook.getSheets();
						if (sheets.length < 1) {
							throw new Exception("无效数据");
						}
						Sheet sheet = sheets[0];
						int rows = sheet.getRows();// 获得行数
						int cols = sheet.getColumns();// 获得列数
						// 读取数据
						model.setRowCount(0);
						for (int col = 0; col < cols; col++) {
							Item item = new Item();
							item.title = sheet.getCell(col, 0).getContents();
							String[] ft = sheet.getCell(col, 1).getContents().toLowerCase().split("~");
							item.from = Integer.valueOf(ft[0].trim());
							item.to = Integer.valueOf(ft[1].trim());
							item.items = new ArrayList<String>();
							for (int row = 2; row < rows; row++) {
								String c = sheet.getCell(col, row).getContents().trim();
								if (!c.isEmpty())
									item.items.add(c);
							}
							model.addRow(
									new Object[] { item.title, item.items.size(), item.from + "~" + item.to, item });
						}
						workbook.close();
						actbtn.doClick();
					}
				} catch (Exception e1) {
					f = null;
					e1.printStackTrace();
					JOptionPane.showMessageDialog(App.this, e1.getMessage());
				}
			}
		});
		actbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Item> items = new ArrayList<Item>();
				for (int row = 0; row < table.getRowCount(); row++) {
					items.add((Item) table.getValueAt(row, 3));
				}
				workPanel.action(items);
			}
		});
		abortbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnPanel.add(abortbtn);
		setVisible(true);
		reloadbtn.doClick();
	}
}
