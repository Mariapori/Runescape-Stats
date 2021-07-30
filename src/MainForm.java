import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.ListModel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Font;
import javax.swing.SwingConstants;

public class MainForm {

	private JFrame frmRunescapeStatsBy;
	public static String pubusername;
	public static List<StatsObject> skillit = new ArrayList<StatsObject>();
	public enum Stats {
		Overall, Attack, Defence, Strength, Constitution, Ranged, Prayer, Magic, Cooking, Woodcutting, Fletching, Fishing, Firemaking, Crafting, Smithing, Mining, Herblore, Agility, Thieving, Slayer, Farming, Runecrafting, Hunter, Construction, Summoning, Dungeoneering, Divination, Invention, Archaeology
	}
	
	public class StatsObject {
		public Stats skills;
		public String lvl;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainForm window = new MainForm();
					window.frmRunescapeStatsBy.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public MainForm() throws IOException {
		readSettings();
		initialize();
	}



	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException {
		frmRunescapeStatsBy = new JFrame();
		frmRunescapeStatsBy.setTitle("Runescape Stats By Mariapori");
		frmRunescapeStatsBy.setBounds(100, 100, 450, 300);
		frmRunescapeStatsBy.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JToolBar toolBar = new JToolBar();
		frmRunescapeStatsBy.getContentPane().add(toolBar, BorderLayout.NORTH);
		
		
		JPanel panel = new JPanel();
		frmRunescapeStatsBy.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		

		
		JLabel lblUsername = new JLabel(pubusername);
		lblUsername.setHorizontalAlignment(SwingConstants.CENTER);
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(lblUsername);
		

		
		if(pubusername != null) {
			URL url = new URL("https://secure.runescape.com/m=hiscore/index_lite.ws?player=" + pubusername);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			
			BufferedReader br = null;
			if (con.getResponseCode() == 200) {
			    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			    String strCurrentLine;
			    int counter = 0;
			        while ((strCurrentLine = br.readLine()) != null) {
			        	try {
			               	String lvl = strCurrentLine.split(",")[1];
			               	StatsObject uus = new StatsObject();
			               	uus.skills = Stats.values()[counter];
			               	uus.lvl = lvl;
			               	skillit.add(uus);
			        		counter++;
			        	}catch(Exception ex) {
			        		
			        	}
			        }
			}			
		}
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String username = JOptionPane.showInputDialog("Runescape nickname",pubusername);
				if(username != null) {
					JSONObject settings = new JSONObject();
					settings.put("username", username);
					try(FileWriter file = new FileWriter("settings.json")){
						file.write(settings.toJSONString());
					}catch(IOException ex){
						JOptionPane.showMessageDialog(null, "Odottamaton virhe!", "Virhe!", JOptionPane.ERROR_MESSAGE);
					}
					pubusername = username;
					lblUsername.setText(username);
				}
			}
		});
		toolBar.add(lblSettings);
		if(skillit.size() > 0) {
			DefaultListModel model = new DefaultListModel();
			for(int i = 0; i < skillit.size(); i++) {
				StatsObject joo = new StatsObject();
				joo = skillit.get(i);
				model.addElement(joo.skills.name() + ": " + joo.lvl);
			}
			JList statslista = new JList(model);
			JScrollPane scrollPane = new JScrollPane();
		    scrollPane.setViewportView(statslista);

			panel.add(scrollPane);
		}
	}
	
	private void readSettings() {
		JSONParser parser = new JSONParser();
		try(Reader reader = new FileReader("settings.json")){
			 JSONObject jsonObject = (JSONObject) parser.parse(reader);
	         String readedUsername = (String)jsonObject.get("username");
	         pubusername = readedUsername;
	         } catch (IOException e) {
	        	 e.printStackTrace();
	        	 } catch (ParseException e) {
	        		 e.printStackTrace();
	        		 }
		}
	}


