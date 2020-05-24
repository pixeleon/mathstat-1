package vadique.khpi.year2.stats.first;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.event.ActionEvent;

public class RegressionController implements Initializable {
	
	private ObservableList<VarsPair> obsList;
	private Double xVar, yVar, covar, r, b0, b1;
	
	@FXML TableView<VarsPair> tableViewVars;
	@FXML LineChart <Number,Number>lineChartRegrLine;
	@FXML TextField textFieldCorrCoeff;
	@FXML TextField textFieldDetermCoeff;
	@FXML TextField textFieldRegrConst;
	@FXML TextField textFieldRegrCoeff;
	@FXML Button buttonAnalyze;
	@FXML TableColumn<VarsPair,Double> tableColumnX;
	@FXML TableColumn<VarsPair,Double> tableColumnY;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableViewVars.setPlaceholder(new Label(""));
		obsList = FXCollections.observableArrayList(
				new VarsPair(7, 30),
				new VarsPair(9, 30),
				new VarsPair(10, 25),
				new VarsPair(19, 14),
				new VarsPair(20, 20),
				new VarsPair(48, 33)
				);
		initTable();
	}
	
	private void initList() {
		List<VarsPair> list = new ArrayList<VarsPair>();
		obsList = FXCollections.observableList(list);
	
	}
	
	private void initTable() {
	    tableViewVars.setItems(obsList);
	    tableColumnX.setCellValueFactory(new PropertyValueFactory<>("x"));
	    tableColumnX.setCellFactory(
	            TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    tableColumnX.setOnEditCommit(t -> (t.getTableView().getItems().get(t.getTablePosition().getRow()))
	    		.setX(t.getNewValue()));
	    tableColumnY.setCellValueFactory(new PropertyValueFactory<>("y"));
	    tableColumnY.setCellFactory(
	            TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
	    tableColumnY.setOnEditCommit(t -> (t.getTableView().getItems().get(t.getTablePosition().getRow()))
	    		.setY(t.getNewValue()));
	}

	@FXML public void doAdd(ActionEvent event) {
		if (obsList == null) {
			initList();
		}
		obsList.add(new VarsPair(0,0));
		initTable();
	}

	@FXML public void doRemove(ActionEvent event) {
		if (obsList == null) {
            return;
        }
        if (obsList.size() > 0) {
            obsList.remove(obsList.size() - 1);
        }
        if (obsList.size() <= 0) {
            obsList = null;
        }
	}
	
	@FXML public void doClear(ActionEvent event) {
		textFieldCorrCoeff.setText("");
		textFieldDetermCoeff.setText("");
		textFieldRegrConst.setText("");
		textFieldRegrCoeff.setText("");
		tableViewVars.setItems(null);
		tableViewVars.setPlaceholder(new Label(""));
		lineChartRegrLine.getData().clear();
		initList();
		initTable();
	}

	@FXML public void doAnalyze(ActionEvent event) {
		if (obsList.size() <= 1) {
			return;
		}
		xVar = getXVar(getXMean());
		yVar= getYVar(getYMean());
		covar = getCovar(getXMean(), getYMean());
		r = getCorrCoeff();
		b1 = getB1();
		b0 = getB0();
		updateTextFields();
		updateGaph();
	}
	
	public double getXMean() {
		double xTotal = 0;
		for(int i = 0; i < obsList.size(); i++) {
			xTotal += obsList.get(i).getX();
		}
		return xTotal / obsList.size();
	}
	
	public double getYMean() {
		double yTotal = 0;
		for(int i = 0; i < obsList.size(); i++) {
			yTotal += obsList.get(i).getY();
		}
		return yTotal / obsList.size();
	}
	
	public double getXVar(double xMean) {
		double xSum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double xi = obsList.get(i).getX() - xMean;
			xSum += xi * xi;
		}
		return xSum / (obsList.size()-1);
	}
	
	public double getYVar(double yMean) {
		double ySum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double yi = obsList.get(i).getY() - yMean;
			ySum += yi * yi;
		}
		return ySum / (obsList.size()-1);
	}
	
	public double getCovar(double xMean, double yMean) {
		double xySum = 0;
		for(int i = 0; i < obsList.size(); i++) {
			double xi = obsList.get(i).getX() - xMean;
			double yi = obsList.get(i).getY() - yMean;
			xySum += xi * yi;
		}
		return xySum / (obsList.size()-1);
	}
	
	public double getCorrCoeff() {
		return covar / (Math.sqrt(xVar) * Math.sqrt(yVar));
	}
	
	public double getB1() {
		return r * Math.sqrt(yVar) / Math.sqrt(xVar);
	}
	
	public double getB0() {
		return getYMean() - b1 * getXMean();
	}
	
	public double getXMax() {
		double xMax = obsList.get(0).getX();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getX() > xMax) {
				xMax = obsList.get(i).getX();			
			}
		}
		return xMax;
	}

	private double getXMin() {
		double xMin = obsList.get(0).getX();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getX() < xMin) {
				xMin = obsList.get(i).getX();			
			}
		}
		return xMin;
	}
	
	public double getYMax() {
		double yMax = obsList.get(0).getY();
		for (int i = 1; i < obsList.size(); i++) {
			if (obsList.get(i).getY() > yMax) {
				yMax = obsList.get(i).getY();			
			}
		}
		return yMax;
	}
	
	private void updateTextFields() {
		textFieldCorrCoeff.setText(r + "");
		textFieldDetermCoeff.setText((r*r) + "");
		textFieldRegrConst.setText(b0 + "");
		textFieldRegrCoeff.setText(b1 + "");
	}
	
	private void updateGaph() {
		lineChartRegrLine.getData().clear();
        double h = (getXMax()- getXMin()) / 1000;
		XYChart.Series<Number, Number> regrSeries = new XYChart.Series<>();
		for (double x = 0;  x < getXMax(); x += h)  {
			regrSeries.getData().add(new XYChart.Data<>(x, predY(x)));
		}
		lineChartRegrLine.getData().add(regrSeries);
		for (int i = 0; i < obsList.size(); i++) {
			XYChart.Series<Number, Number> point = new XYChart.Series<>();
			point.getData().add(new XYChart.Data<Number, Number>
						(obsList.get(i).getX(), obsList.get(i).getY()));
			lineChartRegrLine.getData().add(point);
		}
	}
	
	private double predY(double x) {
		return b0 + b1*x;
	}

}
