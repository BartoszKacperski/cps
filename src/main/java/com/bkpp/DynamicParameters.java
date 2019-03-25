package com.bkpp;

import com.bkpp.signals.Signal;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DynamicParameters extends VBox {
    private Signal signal;
    private List<Field> fields;
    private HashMap<Field, TextField> textFieldHashMap;

    public DynamicParameters(Signal signal){
        this.signal = signal;
        this.fields = new ArrayList<>();
        this.textFieldHashMap = new HashMap<>();
        init();
    }

    private void init() {
        Class c = signal.getClass();

        do {
            for(Field field : c.getDeclaredFields()){
                if(!field.getName().equals("points")){
                    fields.add(field);
                }
            }
            c = c.getSuperclass();
        }while (c.getSuperclass() != null);

        initView();
    }

    private void initView() {
        fields.forEach( f -> {
            this.getChildren().add(createNewNode(f));
        });
    }

    private Node createNewNode(Field f) {
        VBox vBox = new VBox();
        vBox.setPrefSize(150, 40);

        Label label = new Label();
        label.setPrefSize(150, 20);
        label.setText(f.getName());

        TextField textField = new TextField();
        textField.setId(f.getName());
        textField.setPrefSize(150, 20);
        textField.setPromptText(f.getName());

        vBox.getChildren().add(label);
        vBox.getChildren().add(textField);

        textFieldHashMap.put(f, textField);

        return vBox;
    }

    public Signal getInitializedSignal() throws IllegalAccessException {
        for(Field field : fields){
            Double value = Double.parseDouble(textFieldHashMap.get(field).getText());
            field.setAccessible(true);
            field.set(signal, value);
        }

        return this.signal;
    }
}
