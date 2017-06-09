/**
 * Created by Accident on 09/06/2017.
 */

let no_weekly_data = [];
let before_weekly_data = [];
let after_weekly_data = [];

function setWeeklyData(data) {
    no_weekly_data = get_no_KLineData(data);
    before_weekly_data = get_before_KLineData(data);
    after_weekly_data = get_after_KLineData(data);
}