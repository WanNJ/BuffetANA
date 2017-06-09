/**
 * Created by Accident on 09/06/2017.
 */
let no_monthly_data = [];
let before_monthly_data = [];
let after_monthly_data = [];

function setMonthlyData(data) {
    no_monthly_data = get_no_KLineData(data);
    before_monthly_data = get_before_KLineData(data);
    after_monthly_data = get_after_KLineData(data);
}