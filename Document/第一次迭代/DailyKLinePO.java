import java.time.LocalDate;

public class DailyKLinePO {
	
	private String name;
	private String market;
	private String code;
	private LocalDate date;
	private double high_Price;
	private double low_Price;
	private double open_Price;
	private double close_Price;
	private StockState state;
	private long volume;
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public double getHigh_Price() {
		return high_Price;
	}
	public void setHigh_Price(double high_Price) {
		this.high_Price = high_Price;
	}
	public double getLow_Price() {
		return low_Price;
	}
	public void setLow_Price(double low_Price) {
		this.low_Price = low_Price;
	}
	public double getOpen_Price() {
		return open_Price;
	}
	public void setOpen_Price(double open_Price) {
		this.open_Price = open_Price;
	}
	public double getClose_Price() {
		return close_Price;
	}
	public void setClose_Price(double close_Price) {
		this.close_Price = close_Price;
	}
	public StockState getState() {
		return state;
	}
	public void setState(StockState state) {
		this.state = state;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
}
