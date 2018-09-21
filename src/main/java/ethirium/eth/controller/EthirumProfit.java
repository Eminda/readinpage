package ethirium.eth.controller;

public class EthirumProfit {
    private String profit;
    private String ethirium;
    private String cost;

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getEthirium() {
        return ethirium;
    }

    public void setEthirium(String ethirium) {
        this.ethirium = ethirium;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EthirumProfit{");
        sb.append("profit='").append(profit).append('\'');
        sb.append(", ethirium='").append(ethirium).append('\'');
        sb.append(", cost='").append(cost).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
