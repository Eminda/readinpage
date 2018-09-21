package ethirium.eth.controller;

public class Ethirum {
    private String profitRatePerDay;
    private String monthlyProfit;
    private EthirumProfit dayProfit;
    private EthirumProfit weekProfit;
    private EthirumProfit monthProfit;
    private EthirumProfit yearProfit;


    public String getProfitRatePerDay() {
        return profitRatePerDay;
    }

    public void setProfitRatePerDay(String profitRatePerDay) {
        this.profitRatePerDay = profitRatePerDay;
    }

    public String getMonthlyProfit() {
        return monthlyProfit;
    }

    public void setMonthlyProfit(String monthlyProfit) {
        this.monthlyProfit = monthlyProfit;
    }

    public EthirumProfit getDayProfit() {
        return dayProfit;
    }

    public void setDayProfit(EthirumProfit dayProfit) {
        this.dayProfit = dayProfit;
    }

    public EthirumProfit getWeekProfit() {
        return weekProfit;
    }

    public void setWeekProfit(EthirumProfit weekProfit) {
        this.weekProfit = weekProfit;
    }

    public EthirumProfit getMonthProfit() {
        return monthProfit;
    }

    public void setMonthProfit(EthirumProfit monthProfit) {
        this.monthProfit = monthProfit;
    }

    public EthirumProfit getYearProfit() {
        return yearProfit;
    }

    public void setYearProfit(EthirumProfit yearProfit) {
        this.yearProfit = yearProfit;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Ethirum{");
        sb.append("profitRatePerDay='").append(profitRatePerDay).append('\'');
        sb.append(", monthlyProfit='").append(monthlyProfit).append('\'');
        sb.append(", dayProfit=").append(dayProfit);
        sb.append(", weekProfit=").append(weekProfit);
        sb.append(", monthProfit=").append(monthProfit);
        sb.append(", yearProfit=").append(yearProfit);
        sb.append('}');
        return sb.toString();
    }
}
