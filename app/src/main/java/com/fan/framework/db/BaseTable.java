package com.fan.framework.db;

public interface BaseTable {
	String getColumn(int version);
	String getOldColumn(int version);
	boolean needUpdate(int version);
	boolean keepOldData(int version);
}
