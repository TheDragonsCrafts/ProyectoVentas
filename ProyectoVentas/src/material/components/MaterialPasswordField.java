package material.components;

import javax.swing.JPasswordField;

public class MaterialPasswordField extends JPasswordField {
    private String label;
    public void setLabel(String label) { this.label = label; }
    public String getLabel() { return label; }
}
