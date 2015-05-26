package ikov.pfiremaker;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public GUI() {
		setTitle("PFiremaker");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 200);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JComboBox logComboBox = new JComboBox();
		logComboBox.setToolTipText("Select what log to burn.");
		logComboBox.setModel(new DefaultComboBoxModel(PFiremaker.LOG_NAME));
		logComboBox.setSelectedIndex(PFiremaker.logChoice);
		logComboBox.setBounds(121, 10, 97, 20);
		contentPane.add(logComboBox);
		
		JLabel lblLogType = new JLabel("Log type");
		lblLogType.setBounds(65, 13, 46, 14);
		contentPane.add(lblLogType);
		
		final JComboBox locComboBox = new JComboBox();
		locComboBox.setToolTipText("Select the location you want to start fires.");
		locComboBox.setModel(new DefaultComboBoxModel(PFiremaker.LOCATION));
		locComboBox.setSelectedIndex(PFiremaker.locChoice);
		locComboBox.setSelectedIndex(0);
		locComboBox.setBounds(121, 35, 97, 20);
		contentPane.add(locComboBox);
		
		JLabel lblLocation = new JLabel("Location");
		lblLocation.setBounds(65, 38, 46, 14);
		contentPane.add(lblLocation);
		
		final JCheckBox autoCheckBox = new JCheckBox("Auto progress");
		autoCheckBox.setSelected(true);
		autoCheckBox.setToolTipText("Automatically burn the best type of log.");
		autoCheckBox.setBounds(96, 76, 97, 23);
		contentPane.add(autoCheckBox);
		
		JButton btnNewButton = new JButton("Start");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {
				PFiremaker.logChoice = logComboBox.getSelectedIndex();
				PFiremaker.locChoice = locComboBox.getSelectedIndex();
				PFiremaker.autoProgress = autoCheckBox.isSelected();
				dispose();
			}
		});
		btnNewButton.setBounds(104, 127, 80, 23);
		contentPane.add(btnNewButton);
	}
}
