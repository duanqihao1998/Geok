package com.geok.langfang.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskJosn {

	public JSONObject getJSON() {
		JSONObject Task;
		try {
			// 首先最外层是{}，是创建一个对象
			Task = new JSONObject();
			// 第一个键phone的值是数组，所以需要创建数组对象
			// JSONArray phone = new JSONArray();
			// phone.put("12345678").put("87654321");
			// person.put("phone", phone);

			Task.put("TaskId", "0001");
			Task.put("TaskName", "任务一");
			Task.put("TaskTime", "8:30至9:30");
			Task.put("TaskLine", "兰成渝干线K230至K234");
			Task.put("TaskFrequency", "1次/天");
			// 键address的值是对象，所以又要创建一个对象
			JSONObject Pile = new JSONObject();
			Pile.put("PileType", "电位测试桩");
			Pile.put("PileNum", "K230");
			Pile.put("PileLine", "兰成渝干线");
			Pile.put("PileDistance", "5460301");
			Pile.put("PileIsUsing", "是");
			Pile.put("PileUseDate", "2010-9-8");
			Pile.put("PileTelephone", "没有");
			JSONObject Pile1 = new JSONObject();
			Pile1.put("PileType", "电位测试桩");
			Pile1.put("PileNum", "K231");
			Pile1.put("PileLine", "兰成渝干线");
			Pile1.put("PileDistance", "5460546");
			Pile1.put("PileIsUsing", "否");
			Pile1.put("PileUseDate", "无");
			Pile1.put("PileTelephone", "没有");
			JSONObject Pile2 = new JSONObject();
			Pile2.put("PileType", "电位测试桩");
			Pile2.put("PileNum", "K232");
			Pile2.put("PileLine", "兰成渝干线");
			Pile2.put("PileDistance", "5460786");
			Pile2.put("PileIsUsing", "是");
			Pile2.put("PileUseDate", "2011-1-1");
			Pile2.put("PileTelephone", "没有");
			JSONObject Pile3 = new JSONObject();
			Pile3.put("PileType", "电位测试桩");
			Pile3.put("PileNum", "K233");
			Pile3.put("PileLine", "兰成渝干线");
			Pile3.put("PileDistance", "5461234");
			Pile3.put("PileIsUsing", "是");
			Pile3.put("PileUseDate", "2011-4-1");
			Pile3.put("PileTelephone", "没有");
			JSONObject Pile4 = new JSONObject();
			Pile4.put("PileType", "电位测试桩");
			Pile4.put("PileNum", "K234");
			Pile4.put("PileLine", "兰成渝干线");
			Pile4.put("PileDistance", "5462230");
			Pile4.put("PileIsUsing", "是");
			Pile4.put("PileUseDate", "2011-1-1");
			Pile4.put("PileTelephone", "没有");

			JSONArray pileArray = new JSONArray();
			pileArray.put(Pile);
			pileArray.put(Pile1);
			pileArray.put(Pile2);
			pileArray.put(Pile3);
			pileArray.put(Pile4);
			Task.put("pile", pileArray);
		} catch (JSONException ex) {
			// 键为null或使用json不支持的数字格式(NaN, infinities)
			throw new RuntimeException(ex);
		}

		return Task;
	}

	public List<Map<String, Object>> getSth() {
		List<Pile> list = new ArrayList<Pile>();
		List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		try {
			// JSONTokener jsonParser = new JSONTokener(str);
			// // 此时还未读取任何json文本，直接读取就是一个JSONObject对象。
			// // 如果此时的读取位置在"name" : 了，那么nextValue就是"yuanzhifei89"（String）
			// JSONObject person = (JSONObject) jsonParser.nextValue();
			JSONObject Task = getJSON();

			JSONArray PileArray = Task.getJSONArray("pile");
			for (int i = 0; i < PileArray.length(); i++) {
				JSONObject json = PileArray.getJSONObject(i);
				Pile pile = new Pile();
				pile.setDistance(json.getString("PileDistance"));
				pile.setIsUsing(json.getString("PileIsUsing"));
				pile.setLine(json.getString("PileLine"));
				pile.setTelephone(json.getString("PileTelephone"));
				pile.setUserdate(json.getString("PileUseDate"));
				pile.setPileType(json.getString("PileType"));
				pile.setPileNum(json.getString("PileNum"));
				list.add(pile);
			}
			map = new HashMap<String, Object>();
			map.put("TaskId", Task.get("TaskId"));
			list1.add(map);
			map = new HashMap<String, Object>();
			map.put("TaskName", Task.get("TaskName"));
			list1.add(map);
			map = new HashMap<String, Object>();
			map.put("TaskTime", Task.get("TaskTime"));
			list1.add(map);
			map = new HashMap<String, Object>();
			map.put("TaskLine", Task.get("TaskLine"));
			list1.add(map);
			map = new HashMap<String, Object>();
			map.put("TaskFrequency", Task.get("TaskFrequency"));
			list1.add(map);
			map = new HashMap<String, Object>();
			map.put("pile", list);
			list1.add(map);

		} catch (JSONException ex) {
		}

		return list1;
	}

}
