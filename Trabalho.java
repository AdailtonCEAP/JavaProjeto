
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.swing.JFileChooser;

public class Trabalho extends JPanel {
	private JLabel label_cadastro, label_codigo, label_nome, label_ano, label_genero, label_disponibilidade, nome,
			genero, ano;
	private JButton button_assistir, button_cadastrar, filme;
	private JTextField textfield_codigo, textfield_nome;
	private JFormattedTextField textfield_ano;
	private JRadioButton rbutton_sim, rbutton_nao;
	private ButtonGroup bgroup;
	private JComboBox<?> combobox_genero;
	private JPanel painel, painelFilme;
	private JScrollPane scrollTable;
	private JTable tabela;
	private JPopupMenu menuPopup;
	private JMenuItem itemDeletar, itemExportarXLS, imprimirTudo;
	private FutureTask<JFileChooser> escolherFuturo;
	private DateFormat formato;
	private String[] generos = { "", "Ação", "Aventura", "Comédia", "Drama", "Romance", "Trash", "Terror", "Animação",
			"Musical", "Fantasia" };
	private String caminho;

	private boolean conexao_MySQL = true;
	private String url = "";
	private String user = "";
	private String password = "";

	public Trabalho() {
		inicializarComponentes();
		definirEventos();
	}

	public void inicializarComponentes() {
		setLayout(null);

		formato = new SimpleDateFormat("YYYY");

		label_cadastro = new JLabel("Cadastro de filmes");
		label_codigo = new JLabel("Código");
		nome = new JLabel("Nome");
		ano = new JLabel("Ano");
		genero = new JLabel("Gênero");
		label_nome = new JLabel("Nome");
		label_genero = new JLabel("Gênero");
		label_ano = new JLabel("Ano");
		label_disponibilidade = new JLabel("Disponibilidade");

		rbutton_sim = new JRadioButton("Sim", true);
		rbutton_nao = new JRadioButton("Não");
		bgroup = new ButtonGroup();

		textfield_codigo = new JTextField(5);
		textfield_nome = new JTextField();
		textfield_ano = new JFormattedTextField(formato);
		textfield_ano.setDocument(new JTextFieldLimit(4));

		button_cadastrar = new JButton("Cadastrar");
		button_assistir = new JButton("Assistir");

		combobox_genero = new JComboBox<Object>(generos);

		nome.setBounds(315, 169, 80, 20);
		genero.setBounds(315, 184, 100, 20);
		ano.setBounds(315, 199, 80, 20);
		label_cadastro.setBounds(170, 5, 120, 20);
		label_codigo.setBounds(10, 30, 40, 20);
		label_nome.setBounds(10, 85, 35, 20);
		label_genero.setBounds(125, 85, 45, 20);
		label_ano.setBounds(10, 140, 35, 20);
		label_disponibilidade.setBounds(125, 140, 120, 20);

		textfield_codigo.setBounds(10, 55, 40, 20);
		textfield_nome.setBounds(10, 110, 100, 20);
		textfield_ano.setBounds(10, 165, 100, 20);

		button_cadastrar.setBounds(180, 380, 100, 25);
		button_assistir.setBounds(397, 178, 78, 35);

		rbutton_sim.setBounds(122, 160, 50, 30);
		rbutton_nao.setBounds(170, 160, 50, 30);

		combobox_genero.setBounds(125, 110, 80, 20);

		bgroup.add(rbutton_sim);
		bgroup.add(rbutton_nao);

		add(label_cadastro);
		add(label_codigo);
		add(textfield_codigo);
		add(label_nome);
		add(textfield_nome);
		add(combobox_genero);
		add(label_genero);
		add(label_ano);
		add(textfield_ano);
		add(label_disponibilidade);
		add(rbutton_sim);
		add(rbutton_nao);
		add(button_cadastrar);
		add(nome);
		add(genero);
		add(ano);
		add(button_assistir);

		painelFilme = new JPanel(new BorderLayout());
		painelFilme.setBorder(new TitledBorder("Filme"));
		painelFilme.setBounds(310, 30, 170, 195);

		filme = new JButton("Foto");
		filme.setPreferredSize(new Dimension(200, 120));

		painelFilme.add(filme, BorderLayout.NORTH);
		add(painelFilme);

		painel = new JPanel(new BorderLayout());
		scrollTable = new JScrollPane();

		DefaultTableModel tableModel = new DefaultTableModel(
				new String[] { "Cód.", "Nome", "Gênero", "Ano", "Disp.", "Caminho" }, 0) {
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};

		tabela = new JTable(tableModel);
		menuPopup = new JPopupMenu();
		itemDeletar = new JMenuItem("Deletar linha");
		itemExportarXLS = new JMenuItem("Exportar tudo para Excel (.XLS)");
		imprimirTudo = new JMenuItem("Imprimir todos os dados.");

		menuPopup.add(itemDeletar);
		menuPopup.add(itemExportarXLS);
		menuPopup.add(imprimirTudo);

		DefaultTableCellRenderer alinhaDireita = new DefaultTableCellRenderer();
		alinhaDireita.setHorizontalAlignment(SwingConstants.RIGHT);
		tabela.getColumnModel().getColumn(0).setPreferredWidth(65);
		tabela.getColumnModel().getColumn(0).setResizable(true);
		tabela.getColumnModel().getColumn(1).setPreferredWidth(150);
		tabela.getColumnModel().getColumn(1).setResizable(true);
		tabela.getColumnModel().getColumn(2).setPreferredWidth(90);
		tabela.getColumnModel().getColumn(2).setResizable(true);
		tabela.getColumnModel().getColumn(3).setPreferredWidth(70);
		tabela.getColumnModel().getColumn(3).setResizable(true);
		tabela.getColumnModel().getColumn(4).setPreferredWidth(80);
		tabela.getColumnModel().getColumn(4).setResizable(false);
		tabela.removeColumn(tabela.getColumnModel().getColumn(5));
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		scrollTable.setViewportView(tabela);
		tabela.setComponentPopupMenu(menuPopup);
		painel.add(scrollTable);
		painel.setBounds(10, 235, 470, 140);
		add(painel);

		if (conexao_MySQL) {
			verificarConfiguracao();
		}
	}

	public void verificarConfiguracao() {
		Properties configuracao = new Properties();
		String arquivo_configuracao = System.getProperty("user.dir") + "\\" + "configuracao.properties";

		try {
			File arquivoConf = new File(arquivo_configuracao);

			if (!arquivoConf.exists()) {
				url = JOptionPane.showInputDialog(null,
						"Insira a URL do banco de dados.\nExemplo: mysql://localhost:3306/banco_de_dados",
						"mysql://localhost:3306/");
				user = JOptionPane.showInputDialog(null, "Insira o nome de usuário do banco de dados.\nExemplo: root");
				password = JOptionPane.showInputDialog(null, "Insira a senha do usuário. Opcional.");

				configuracao.setProperty("url", url);
				configuracao.setProperty("user", user);
				configuracao.setProperty("password", password);

				configuracao.store(new FileOutputStream(arquivo_configuracao), null);
			} else {
				configuracao.load(new FileReader(arquivoConf));

				url = configuracao.getProperty("url");
				user = configuracao.getProperty("user");

				if (url == null || user == null || url.equals("") || user.equals("")) {
					url = JOptionPane.showInputDialog(null,
							"Insira a URL do banco de dados.\nExemplo: mysql://localhost:3306/banco_de_dados",
							"mysql://localhost:3306/");
					user = JOptionPane.showInputDialog(null,
							"Insira o nome de usuário do banco de dados.\nExemplo: root");

					try {
						configuracao.setProperty("url", url);
						configuracao.setProperty("user", user);

						configuracao.store(new FileOutputStream(arquivo_configuracao), null);
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void cadastrarMySQL() {
		try {
			String driver = "com.mysql.cj.jdbc.Driver";
			Class.forName(driver);

			Connection conn = DriverManager.getConnection("jdbc:" + url + "?useTimezone=true&serverTimezone=UTC", user,
					password);

			PreparedStatement ps = conn.prepareStatement(
					"INSERT INTO dados(codigo, nome, genero, ano, disponivel, caminho) VALUES(?, ?, ?, ?, ?, ?);");
			ps.setString(1, textfield_codigo.getText());
			ps.setString(2, textfield_nome.getText());
			ps.setString(3, (String) combobox_genero.getSelectedItem());
			ps.setString(4, textfield_ano.getText());

			if (rbutton_sim.isSelected()) {
				ps.setString(5, rbutton_sim.getText());
			} else {
				ps.setString(5, rbutton_nao.getText());
			}

			if (caminho.length() >= 150) {
				ps.setString(6, null);
			} else {
				ps.setString(6, caminho);
			}
			ps.executeUpdate();

		} catch (java.lang.ClassNotFoundException classEx) {
			JOptionPane.showMessageDialog(null,
					"Aparentemente, você não importou o driver certo ou o fez de forma errada.", "Erro.",
					JOptionPane.ERROR_MESSAGE);
			classEx.printStackTrace();
		} catch (com.mysql.cj.jdbc.exceptions.CommunicationsException CommunicationError) {
			JOptionPane.showMessageDialog(null, "Erro ao se conectar no banco de dados.", "Erro!",
					JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void definirEventos() {
		tabela.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				JTable alvo = (JTable) me.getSource();

				int row = alvo.getSelectedRow();

				Image imagem;
				nome.setToolTipText(tabela.getValueAt(row, 1).toString());
				ano.setToolTipText(tabela.getValueAt(row, 3).toString());
				genero.setToolTipText(tabela.getValueAt(row, 2).toString());

				try {
					imagem = ImageIO.read(
							new FileInputStream(tabela.getModel().getValueAt(row, 5).toString()));
					filme.setIcon(new ImageIcon(imagem.getScaledInstance(200, 120, Image.SCALE_SMOOTH)));
					filme.setBorderPainted(false);
					filme.setFocusPainted(false);
					filme.setContentAreaFilled(false);

					nome.setText(tabela.getValueAt(row, 1).toString());
					ano.setText(tabela.getValueAt(row, 3).toString());
					genero.setText(tabela.getValueAt(row, 2).toString());

				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		button_assistir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int linha = tabela.getSelectedRow();
				
				DefaultTableModel dtm = (DefaultTableModel) tabela.getModel();
				
				try {
					dtm.removeRow(linha);
				}
				catch (Exception ex) {
					ex.printStackTrace();
				}
				finally {
					nome.setText("Nome");
					genero.setText("Gênero");
					ano.setText("Ano");
					
					nome.setToolTipText("");
					genero.setToolTipText("");
					ano.setToolTipText("");
					
					filme.setIcon(null);
					filme.setBorderPainted(true);
					filme.setFocusPainted(true);
					filme.setContentAreaFilled(true);
				}
			}
		});

		button_cadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (textfield_codigo.getText().equals("")) {
					JOptionPane.showMessageDialog(painel, "Preencha todos os campos.");
					textfield_codigo.grabFocus();
					textfield_codigo.requestFocus();
					return;
				}

				if (textfield_nome.getText().equals("")) {
					JOptionPane.showMessageDialog(painel, "Preencha todos os campos.");
					textfield_nome.grabFocus();
					textfield_nome.requestFocus();
					return;
				}

				if (textfield_ano.getText().equals("")) {
					JOptionPane.showMessageDialog(painel, "Preencha todos os campos.");
					textfield_ano.grabFocus();
					textfield_ano.requestFocus();
					return;
				}

				if (combobox_genero.getSelectedItem().equals("")) {
					JOptionPane.showMessageDialog(painel, "Preencha todos os campos.");
					combobox_genero.grabFocus();
					combobox_genero.requestFocus();
					return;
				}

				if (filme.getIcon() == null) {
					JOptionPane.showMessageDialog(painel, "Insira uma imagem.");
					filme.grabFocus();
					filme.requestFocus();
					return;
				}

				DefaultTableModel dtm = (DefaultTableModel) tabela.getModel();

				try {
					if (tabela.getModel().getRowCount() >= 1) {
						for (int i = 0; i < tabela.getModel().getRowCount(); i++) {
							if (textfield_codigo.getText().equals((String) tabela.getModel().getValueAt(i, 0))) {
								JOptionPane.showMessageDialog(null, "Este código já existe. Tente outro.", "Erro.",
										JOptionPane.ERROR_MESSAGE);
								break;
							} else {
								if (rbutton_sim.isSelected()) {
									dtm.addRow(new Object[] { textfield_codigo.getText(), textfield_nome.getText(),
											combobox_genero.getSelectedItem(), textfield_ano.getText(),
											rbutton_sim.getText(), caminho });
								} else {
									dtm.addRow(new Object[] { textfield_codigo.getText(), textfield_nome.getText(),
											combobox_genero.getSelectedItem(), textfield_ano.getText(),
											rbutton_nao.getText(), caminho });
								}

								if (conexao_MySQL) {
									cadastrarMySQL();
								}

								limparCampos();
								break;
							}
						}
					} else {
						if (rbutton_sim.isSelected()) {
							dtm.addRow(new Object[] { textfield_codigo.getText(), textfield_nome.getText(),
									combobox_genero.getSelectedItem(), textfield_ano.getText(), rbutton_sim.getText(),
									caminho });
						} else {
							dtm.addRow(new Object[] { textfield_codigo.getText(), textfield_nome.getText(),
									combobox_genero.getSelectedItem(), textfield_ano.getText(), rbutton_nao.getText(),
									caminho });
						}

						if (conexao_MySQL) {
							cadastrarMySQL();
						}

						limparCampos();
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		filme.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				escolherFuturo = new FutureTask<>(JFileChooser::new);
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.execute(escolherFuturo);
				JFileChooser escolherImagem;
				try {
					escolherImagem = escolherFuturo.get();
					
					FileFilter filtrarImagens = new FileNameExtensionFilter("Arquivos de imagens",
							ImageIO.getReaderFileSuffixes());
					escolherImagem.addChoosableFileFilter(filtrarImagens);
					escolherImagem.setCurrentDirectory(new File(System.getProperty("user.home") + "\\Pictures"));

					int resultado = escolherImagem.showOpenDialog(painelFilme);

					if (resultado == JFileChooser.APPROVE_OPTION) {
						File imagem_selecionada = escolherImagem.getSelectedFile();

						try {
							caminho = imagem_selecionada.getAbsolutePath();
							Image imagem = ImageIO.read(new FileInputStream(imagem_selecionada.getAbsolutePath()));
							filme.setIcon(new ImageIcon(imagem.getScaledInstance(200, 120, Image.SCALE_SMOOTH)));
							filme.setBorderPainted(false);
							filme.setFocusPainted(false);
							filme.setContentAreaFilled(false);
						} catch (Exception ex) {
							System.out.println(ex);
						}
					}
				} catch (InterruptedException | ExecutionException e1) {
					e1.printStackTrace();
				}
			}
		});

		imprimirTudo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					boolean completar = tabela.print();

					if (completar) {
						JOptionPane.showMessageDialog(null, "Sucesso.");
					} else {
						JOptionPane.showMessageDialog(null, "Impressão cancelada.");
					}
				} catch (PrinterException pe) {
					JOptionPane.showMessageDialog(null, "Erro ao imprimir: " + pe.getMessage());
				}
			}
		});

		itemDeletar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] linhas = tabela.getSelectedRows();

				DefaultTableModel dtm = (DefaultTableModel) tabela.getModel();
				for (int i = (linhas.length - 1); i >= 0; --i) {
					dtm.removeRow(linhas[i]);
				}
			}
		});

		itemExportarXLS.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					SimpleDateFormat dataFormatacao = new SimpleDateFormat("dd-mm-yyyy");
					Date dataHoje = new Date();

					String nome_arquivo = "cadastros_filmes_" + dataFormatacao.format(dataHoje) + ".xls";

					File arquivo = new File(nome_arquivo);
					TableModel modeloXLS = tabela.getModel();
					FileWriter excel = new FileWriter(arquivo);

					for (int i = 0; i < modeloXLS.getColumnCount(); i++) {
						excel.write(modeloXLS.getColumnName(i) + "\t");
					}

					excel.write("\n");

					for (int i = 0; i < modeloXLS.getRowCount(); i++) {
						for (int j = 0; j < modeloXLS.getColumnCount(); j++) {
							String dado = (String) modeloXLS.getValueAt(i, j);

							if (dado == "null") {
								dado = "";
							}
							excel.write(dado + "\t");
						}
						excel.write("\n");
					}
					excel.close();
					JOptionPane.showMessageDialog(null, "Arquivo " + nome_arquivo + " criado.");
				} catch (IOException ex) {
					JOptionPane.showMessageDialog(null, "Erro ao criar arquivo.", "Erro: " + ex.getMessage(), 1);
				}
			}
		});
	}

	void limparCampos() {
		textfield_codigo.setText("");
		textfield_nome.setText("");
		textfield_ano.setText("");
		combobox_genero.setSelectedItem("");
		filme.setIcon(null);
		filme.setBorderPainted(true);
		filme.setFocusPainted(true);
		filme.setContentAreaFilled(true);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Cadastro de filmes");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new Trabalho());
		frame.setBounds(450, 300, 495, 443);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
