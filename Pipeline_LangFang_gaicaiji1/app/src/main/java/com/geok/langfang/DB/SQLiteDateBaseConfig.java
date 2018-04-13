package com.geok.langfang.DB;

public class SQLiteDateBaseConfig {
	private static final String s_DataBaseName = "pipeline.db";
	private static final int s_Version = 4;

	public static String GetDataBaseName() {
		return s_DataBaseName;
	}

	public static int GetVersion() {
		return s_Version;
	}
}
