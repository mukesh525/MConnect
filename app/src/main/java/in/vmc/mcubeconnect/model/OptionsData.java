package in.vmc.mcubeconnect.model;


public class OptionsData {

    private String OptionId;
    private String OptionName;
    private boolean IsChecked;


    public OptionsData() {

    }


    public String getOptionId() {
        return OptionId;
    }

    public void setOptionId(String optionId) {
        OptionId = optionId;
    }

    public String getOptionName() {
        return OptionName;
    }

    public void setOptionName(String optionName) {
        OptionName = optionName;
    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean isChecked) {
        IsChecked = isChecked;
    }


}

