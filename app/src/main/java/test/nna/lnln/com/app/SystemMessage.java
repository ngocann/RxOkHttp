package test.nna.lnln.com.app;

/**
 * Created by DELL on 11/10/2017.
 */

public class SystemMessage {

    /**
     * InformationId : 1
     * StartDate : 2017/08/01T00:00:00
     * EndDate : 2017/09/30T00:00:00
     * Title : æœ¬ç•ªã‚¿ã‚¤ãƒˆãƒ«!
     * Body : ãƒ¡ãƒƒã‚»ãƒ¼ã‚¸ã§ã™
     * PositiveButton : ã¯ã„
     * NegativeButton : ã„ã„ãˆ
     */

    private int InformationId;
    private String StartDate;
    private String EndDate;
    private String Title;
    private String Body;
    private String PositiveButton;
    private String NegativeButton;

    public int getInformationId() {
        return InformationId;
    }

    public void setInformationId(int InformationId) {
        this.InformationId = InformationId;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String Body) {
        this.Body = Body;
    }

    public String getPositiveButton() {
        return PositiveButton;
    }

    public void setPositiveButton(String PositiveButton) {
        this.PositiveButton = PositiveButton;
    }

    public String getNegativeButton() {
        return NegativeButton;
    }

    public void setNegativeButton(String NegativeButton) {
        this.NegativeButton = NegativeButton;
    }

    @Override
    public String toString() {
        return "SystemMessage{" +
                "InformationId=" + InformationId +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", Title='" + Title + '\'' +
                ", Body='" + Body + '\'' +
                ", PositiveButton='" + PositiveButton + '\'' +
                ", NegativeButton='" + NegativeButton + '\'' +
                '}';
    }
}
