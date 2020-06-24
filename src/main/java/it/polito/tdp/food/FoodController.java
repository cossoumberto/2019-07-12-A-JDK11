/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.Graphs;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.FoodPeso;
import it.polito.tdp.food.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	Integer portion = null;
    	try {
    		portion = Integer.parseInt(txtPorzioni.getText());
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.appendText("Inserimento non valido");
    	}
    	if(portion!=null) {
    		model.creaGrafo(portion);
    		boxFood.getItems().addAll(model.getGrafo().vertexSet()); //TENDINA NON ORDINATA
    		txtResult.appendText("Grafo creato con " + model.getGrafo().vertexSet().size() + " vertici e " 
    				+ model.getGrafo().edgeSet().size() + " archi");
    	} 
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	if(boxFood.getValue()!=null) {
    		if(model.listFoodPeso(boxFood.getValue()).size()==0) {
    			txtResult.appendText("Vertice non connesso");
    		} else {
    		int i = 0;
	    		for(FoodPeso fp : model.listFoodPeso(boxFood.getValue())) {
	    			i++;
	    			if(i<=5)
	    				txtResult.appendText(fp.toString()+"\n");
	    		}
    		}
    	} else
    		txtResult.appendText("Seleziona un food");
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	Integer k = null;
    	try {
    		k = Integer.parseInt(txtK.getText());
    	} catch (NumberFormatException e) {
    		e.printStackTrace();
    		txtResult.appendText("Inserimento non valido");
    	}
    	if(boxFood.getValue()==null) {
    		txtResult.appendText("Seleziona un food");
    		return;
    	}
    	if(k!=null) {
    		Integer i = model.simulazione(k, boxFood.getValue());
    		txtResult.appendText("Sono stati preparati " + i + " cibi, in " + model.tempoPreparazione() + " minuti");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
