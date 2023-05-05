module org.mehrizi.crazydictionary {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
//    requires slobj;
//    requires slobj;
    requires org.json;

    opens org.mehrizi.crazydictionary to javafx.fxml;
    exports org.mehrizi.crazydictionary;
}