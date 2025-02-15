package application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.mysql.cj.jdbc.MysqlDataSource;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;

import javafx.scene.control.Label;

import javafx.scene.control.TextArea;

import javafx.scene.control.ComboBox;

import javafx.scene.control.TableView;

import javafx.scene.control.DatePicker;

import javafx.scene.control.TableColumn;

public class Okno_requestController implements Initializable{
	@FXML
	private Label polzvtel;
	@FXML
	private TableView<Request> table;
	@FXML
	private TableColumn<Request, Integer> requestID;
	@FXML
	private TableColumn<Request, String> startDate;
	@FXML
	private TableColumn<Request, String> computerTechType;
	@FXML
	private TableColumn<Request, String> computerTechModel;
	@FXML
	private TableColumn<Request, String> problemDescryption;
	@FXML
	private TableColumn<Request, String> requestStatus;
	@FXML
	private TableColumn<Request, String> completionDate;
	@FXML
	private TableColumn<Request, String> repairParts;
	@FXML
	private TableColumn<Request, Integer> masterID;
	@FXML
	private TableColumn<Request, Integer> clientID;
	@FXML
	private Button vixod;
	@FXML
	private DatePicker datapic;
	@FXML
	private TextField vid_texniki;
	@FXML
	private TextField model_texniki;
	@FXML
	private TextArea opisanie_problem;
	@FXML
	private ComboBox<String> status;
	@FXML
	private ComboBox<String> master;
	@FXML
	private Label Kol_zav;
	@FXML
	private Button add;
	@FXML
	private TextField poisk;
	@FXML
	private Button sbros;
	
	
	
	RequestDao requestDao;
	ObservableList<Request> zav = FXCollections.observableArrayList();
	String polzovatel;
	ObservableList<String> statusValues = FXCollections.observableArrayList("новая заявка");
	MysqlDataSource dataSource;
	JdbcTemplate jb;
	// Event Listener on Button[#vixod].onAction
	@FXML
	public void act_vixod(ActionEvent event) throws IOException {
		// TODO Autogenerated
		Pane root=null;
		root = (Pane)FXMLLoader.load(getClass().getResource("Vxod.fxml"));
		Stage stage = (Stage) vixod.getScene().getWindow();
		stage.setScene(new Scene(root,600,400));
	}
	// Event Listener on Button[#add].onAction
	@FXML
	public void act_add(ActionEvent event) {
		// TODO Autogenerated
		
		
		
	}
	// Event Listener on Button[#sbros].onAction
	@FXML
	public void act_sbros(ActionEvent event) {
		// TODO Autogenerated
		zav.clear();
		poisk.clear();
		zav.addAll(requestDao.getAll());
		table.setItems(zav);
	}
	
	public void setCurrentUser(String login2) {
		// TODO Auto-generated method stub
		jb = new JdbcTemplate(dataSource);
		var rez =  jb.query("Select userID from datausers where login='"+login2+"'", (resultSet, rowNum) -> {return resultSet.getString("userID");});
		polzovatel = rez.get(0);

		String sql = "SELECT name, surname FROM datausers WHERE userID = ?";
		Map<String, Object> userData = jb.queryForMap(sql, polzovatel);
		// Извлечение имени и фамилии из результата запроса
		String name = (String) userData.get("name");
		String surname = (String) userData.get("surname");
		// Установка имени и фамилии на Label
		polzvtel.setText("Пользователь: "+name +" "+ surname);
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		dataSource = new MysqlDataSource();
		dataSource.setServerName("localhost");
		dataSource.setPort(3306);
		dataSource.setDatabaseName("dem");
		dataSource.setUser("root");
		dataSource.setPassword("");
		jb = new JdbcTemplate(dataSource);
		
		status.setItems(statusValues);
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		// Выполнение запроса с помощью JdbcTemplate
		List<String> items = jdbcTemplate.query("SELECT userID, name FROM masteruser", new RowMapper<String>() {
		    public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		        return rs.getString("userID") + " - " + rs.getString("name");
		    }
		});
		// Создание ObservableList и установка элементов в ComboBox
		ObservableList<String> observableItems = FXCollections.observableArrayList(items);
		master.setItems(observableItems);
		
		
		requestDao = new RequestDao(dataSource);
		zav.addAll(requestDao.getAll());
		requestID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("requestID"));
		startDate.setCellValueFactory(new PropertyValueFactory<Request, String>("startDate"));
		computerTechType.setCellValueFactory(new PropertyValueFactory<Request, String>("computerTechType"));
		computerTechModel.setCellValueFactory(new PropertyValueFactory<Request, String>("computerTechModel"));
		problemDescryption.setCellValueFactory(new PropertyValueFactory<Request, String>("problemDescryption"));
		requestStatus.setCellValueFactory(new PropertyValueFactory<Request, String>("requestStatus"));
		completionDate.setCellValueFactory(new PropertyValueFactory<Request, String>("completionDate"));
		repairParts.setCellValueFactory(new PropertyValueFactory<Request, String>("repairParts"));
		masterID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("masterID"));
		clientID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("clientID"));
		table.setItems(zav);
		
		// Напишите SQL-запрос для подсчета количества заявок со статусом "завершена"
				String sql = "SELECT COUNT(*) FROM datarequests WHERE requestStatus = 'готова к выдаче'";
				// Используйте JdbcTemplate для выполнения запроса и получения результата
				int count = jdbcTemplate.queryForObject(sql, Integer.class);
				// Установите значение в Label
				Kol_zav.setText("Количество завершённых заявок: "+String.valueOf(count));

				poisk.textProperty().addListener((observable, oldValue, newValue) -> {
				    search(newValue);
				});
	}
	
	private void search(String number) {
	    FilteredList<Request> filteredData = new FilteredList<>(table.getItems(), p -> true);
	    filteredData.setPredicate(remont -> {
	        if (number == null || number.isEmpty()) {
	            return true;
	        }
	        String lowerCaseFilter = number.toLowerCase();
	        if (remont.getComputerTechModel().toLowerCase().contains(lowerCaseFilter)) {
	            return true;
	        }
	        return false;
	    });
	    SortedList<Request> sortedData = new SortedList<>(filteredData);
	    sortedData.comparatorProperty().bind(table.comparatorProperty());
	    table.setItems(sortedData);
	}
	
	
}
