package zone.mcw.mcwzone.springbootmcwform.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author W4i
 * @date 2020/6/10 14:56
 */
public class Time {
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

	/**
	 * 获取当前时间
	 *
	 * @return
	 */
	public String getTime() {
		String time = df.format(new Date());
		return time;
	}

	/**
	 * 传入某个时间的String格式，用于和当前时间比较
	 * 若小于当前时间，则返回false，反之true
	 *
	 * @param time
	 * @return
	 */
	public boolean timeCompareWithNow(String time) {
		Date date = timeToDate(time);
		String t = getTime();
		Date now = timeToDate(t);
		if (date.before(now)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 传入某个时间的String格式以及增加的时间（分钟为单位），用于和当前时间比较
	 * 若传入的时间加上增加的时间小于当前时间，则返回false，反之true
	 *
	 * @param time
	 * @param moreTime
	 * @return
	 */
	public boolean timeCompareWithNow(String time, long moreTime) {
		moreTime = moreTime * 60 * 1000;
		Date date = timeToDate(time);
		String t = getTime();
		Date now = timeToDate(t);
		date = new Date(date.getTime() + moreTime);
		if (date.before(now)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 将String类型的时间转为Date
	 *
	 * @param time
	 * @return
	 */
	public Date timeToDate(String time) {
		Date data1 = null;
		try {
			data1 = df.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		data1 = new Date(data1.getTime());
		return data1;
	}

	public int timeSubtraction(String longer, String shorter) {
		Date data1 = timeToDate(longer);
		Date data2 = timeToDate(shorter);
		long subtraction = (data1.getTime() - data2.getTime()) / 1000;
		return (int) (subtraction / 60);
	}
}




