import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Interfaz extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private String re = "";
	private String texto = "";
	private Container container;
	private JLabel titleLabel;
	private JTextArea resultTextArea;
	private JButton loadERButton;
	private JButton loadTextButton;
	private JButton executeButton;
	private JScrollPane scrollPaneArea;
	private JFileChooser fileChooser;

	private Controlador interfaz = null;

	public Interfaz() {

		interfaz = new Controlador();
		container = getContentPane();
		container.setLayout(null);
		fileChooser = new JFileChooser();

		titleLabel = new JLabel();
		titleLabel.setBounds(110, 20, 180, 23);

		resultTextArea = new JTextArea();
		resultTextArea.setLineWrap(true);
		resultTextArea.setWrapStyleWord(true);
		scrollPaneArea = new JScrollPane();
		scrollPaneArea.setBounds(20, 50, 550, 270);
		scrollPaneArea.setViewportView(resultTextArea);

		loadERButton = new JButton();
		loadERButton.setText("Expresión Regular");
		loadERButton.setBounds(20, 330, 120, 23);
		loadERButton.addActionListener(this);

		loadTextButton = new JButton();
		loadTextButton.setText("Texto");
		loadTextButton.setBounds(140, 330, 120, 23);
		loadTextButton.addActionListener(this);

		executeButton = new JButton();
		executeButton.setText("Procesar");
		executeButton.setBounds(450, 330, 120, 23);
		executeButton.addActionListener(this);

		container.add(titleLabel);
		container.add(scrollPaneArea);
		container.add(loadERButton);
		container.add(loadTextButton);
		container.add(executeButton);

		setTitle("PROYECTO FINAL: Esteban Cervantes, Carlos Rubio, Oscar Cardenas");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadERButton) {
			cargarER();
			String txt = this.resultTextArea.getText();
			txt += "Expresión Regular: \n" + this.interfaz.getRegularExpression() + "\n\n";
			resultTextArea.setText(txt);
		} else if (e.getSource() == loadTextButton) {
			cargarTexto();
			String txt = this.resultTextArea.getText();
			txt += "Texto: \n" + this.interfaz.getText() + "\n\n";
			resultTextArea.setText(txt);
		} else if (e.getSource() == executeButton) {
			interfaz.compute();
		}
	}

	private void cargarTexto() {
		try {

			fileChooser.showOpenDialog(this);
			File fileText = fileChooser.getSelectedFile();
			List<String> myText = Files.readAllLines(Paths.get(fileText.getAbsolutePath()));
			for (String txt : myText) {
				this.texto += (txt + "\n");
			}
			interfaz.setText(this.texto);
		} catch (Exception e) {

		}
	}

	private void cargarER() {
		try {
			fileChooser.showOpenDialog(this);
			File fileRegularExpression = fileChooser.getSelectedFile();
			List<String> myRegularExpression = Files.readAllLines(Paths.get(fileRegularExpression.getAbsolutePath()));
			for (String er : myRegularExpression) {
				this.re += er;
			}
			interfaz.setRegularExpression(this.re);
		} catch (Exception e) {

		}
	}

}