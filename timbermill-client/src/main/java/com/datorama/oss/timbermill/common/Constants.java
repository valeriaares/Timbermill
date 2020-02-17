package com.datorama.oss.timbermill.common;

import java.time.ZonedDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Constants {
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeConverter()).create();

	public static final int TWO_MB = 2097152;
	public static final String DEFAULT_ELASTICSEARCH_URL = "http://localhost:9200";
	public static final String DEFAULT_TIMBERMILL_URL = "http://localhost:8484";
	public static final String LOG_WITHOUT_CONTEXT = "LogWithoutContext";
	public static final String HEARTBEAT_TASK = "metadata_timbermill_client_heartbeat";
	public static final String EXCEPTION = "exception";
	public static final String TIMBERMILL_INDEX_PREFIX = "timbermill";
	public static final String INDEX_DELIMITER = "-";
	public static final String TYPE = "_doc";
	public static final String TIMBERMILL_SCRIPT = "timbermill-script";
	public static final String ALREADY_STARTED = "ALREADY_STARTED";
	public static final String ALREADY_CLOSED = "ALREADY_CLOSED";
	public static final String CORRUPTED_REASON = "corruptedReason";
	public static final String MAPPING = "   {\"dynamic_templates\": [\n"
			+ "      {\n"
			+ "        \"env\": {\n"
			+ "          \"path_match\":   \"env\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "            {\n"
			+ "        \"name\": {\n"
			+ "          \"path_match\":   \"name\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "            {\n"
			+ "        \"status\": {\n"
			+ "          \"path_match\":   \"status\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "            {\n"
			+ "        \"parentId\": {\n"
			+ "          \"path_match\":   \"parentId\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "            {\n"
			+ "        \"primaryId\": {\n"
			+ "          \"path_match\":   \"primaryId\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "                  {\n"
			+ "        \"log\": {\n"
			+ "          \"path_match\":   \"log\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"text\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "      {\n"
			+ "        \"context\": {\n"
			+ "          \"path_match\":   \"ctx.*\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "      {\n"
			+ "        \"string\": {\n"
			+ "          \"path_match\":   \"string.*\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"keyword\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "      {\n"
			+ "        \"text\": {\n"
			+ "          \"path_match\":   \"text.*\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"text\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      },\n"
			+ "            {\n"
			+ "        \"metric\": {\n"
			+ "          \"path_match\":   \"metric.*\",\n"
			+ "          \"mapping\": {\n"
			+ "            \"type\":       \"double\"\n"
			+ "          }\n"
			+ "        }\n"
			+ "      }\n"
			+ "    ]\n"
			+ "  }\n"
			+ "}";
	public static final String SCRIPT =
			"if (params.orphan != null && !params.orphan) {"
					+ "            ctx._source.orphan = false;        "
					+ "}        "
					+ "if (params.dateToDelete != null) {"
					+ "            ctx._source.meta.dateToDelete = params.dateToDelete;        "
					+ "}        "
					+ "if (params.status != null){"
					+ "        if (ctx._source.string == null){"
					+ "                ctx._source.string =  new HashMap();"
					+ "        }"
					+ "        if (params.status.equals( \\\"CORRUPTED\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "        }"
					+ "        if (ctx._source.status.equals( \\\"SUCCESS\\\" ) || ctx._source.status.equals( \\\"ERROR\\\" )){"
					+ "            if(params.status.equals( \\\"SUCCESS\\\" ) || params.status.equals( \\\"ERROR\\\" )){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"UNTERMINATED\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_STARTED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_SUCCESS\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_ERROR\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "        }"
					+ "        else if (ctx._source.status.equals( \\\"UNTERMINATED\\\")){"
					+ "            if(params.status.equals( \\\"SUCCESS\\\" ) || params.status.equals( \\\"ERROR\\\" )){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_STARTED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"UNTERMINATED\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_STARTED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_SUCCESS\\\")){"
					+ "                long taskBegin = ZonedDateTime.parse(ctx._source.meta.taskBegin, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();"
					+ "                ctx._source.meta.duration = params.taskEndMillis - taskBegin;"
					+ "                ctx._source.meta.taskEnd = params.taskEnd;"
					+ "                ctx._source.status =  \\\"SUCCESS\\\" ;"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_ERROR\\\")){"
					+ "                long taskBegin = ZonedDateTime.parse(ctx._source.meta.taskBegin, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();"
					+ "                ctx._source.meta.duration = params.taskEndMillis - taskBegin;"
					+ "                ctx._source.meta.taskEnd = params.taskEnd;"
					+ "                ctx._source.status = \\\"ERROR\\\";"
					+ "            }"
					+ "        }"
					+ "        else if (ctx._source.status.equals( \\\"PARTIAL_SUCCESS\\\")){"
					+ "            if(params.status.equals( \\\"SUCCESS\\\" ) || params.status.equals( \\\"ERROR\\\" )){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            if (params.status.equals( \\\"UNTERMINATED\\\")){"
					+ "                long taskEnd = ZonedDateTime.parse(ctx._source.meta.taskEnd, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();"
					+ "                ctx._source.meta.duration = taskEnd - params.taskBeginMillis;"
					+ "                ctx._source.meta.taskBegin = params.taskBegin;"
					+ "                ctx._source.status =  \\\"SUCCESS\\\" ;"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_SUCCESS\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_ERROR\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "        }"
					+ "        else if (ctx._source.status.equals( \\\"PARTIAL_ERROR\\\")){"
					+ "            if(params.status.equals( \\\"SUCCESS\\\" ) || params.status.equals( \\\"ERROR\\\" )){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"UNTERMINATED\\\")){"
					+ "                long taskEnd = ZonedDateTime.parse(ctx._source.meta.taskEnd, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant().toEpochMilli();"
					+ "                ctx._source.meta.duration = taskEnd - params.taskBeginMillis;"
					+ "                ctx._source.meta.taskBegin = params.taskBegin;"
					+ "                ctx._source.status =  \\\"ERROR\\\" ;"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_SUCCESS\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_ERROR\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "                ctx._source.string.put(\\\"corruptedReason\\\",\\\"ALREADY_CLOSED\\\");"
					+ "            }"
					+ "            else if (params.status.equals( \\\"CORRUPTED\\\")){"
					+ "                ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "            }"
					+ "        }"
					+ "        else if (ctx._source.status.equals( \\\"PARTIAL_INFO_ONLY\\\")){"
					+ "            if(params.status.equals( \\\"SUCCESS\\\" ) || params.status.equals( \\\"ERROR\\\" )){"
					+ "                ctx._source.meta.duration = params.taskEndMillis - params.taskBeginMillis;"
					+ "                ctx._source.meta.taskEnd = params.taskEnd;"
					+ "                ctx._source.meta.taskBegin = params.taskBegin;"
					+ "                ctx._source.status = params.status;"
					+ "            }"
					+ "            else if (params.status.equals( \\\"UNTERMINATED\\\")){"
					+ "                ctx._source.meta.taskBegin = params.taskBegin;"
					+ "                ctx._source.status = params.status;"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_SUCCESS\\\")){"
					+ "                ctx._source.meta.taskEnd = params.taskEnd;"
					+ "                ctx._source.status = params.status;"
					+ "            }"
					+ "            else if (params.status.equals( \\\"PARTIAL_ERROR\\\")){"
					+ "                ctx._source.meta.taskEnd = params.taskEnd;"
					+ "                ctx._source.status = params.status;"
					+ "            }"
					+ "        }"
					+ "        else {"
					+ "            ctx._source.status =  \\\"CORRUPTED\\\" ;"
					+ "        }"
					+ "        }"
					+ "        if (params.contx != null) {"
					+ "            if (ctx._source.ctx == null) {"
					+ "                ctx._source.ctx = params.contx;"
					+ "            }"
					+ "            else {"
					+ "                ctx._source.ctx.putAll(params.contx);"
					+ "            }"
					+ "        }"
					+ "        if (params.string != null) {"
					+ "            if (ctx._source.string == null) {"
					+ "                ctx._source.string = params.string;"
					+ "            }"
					+ "            else {"
					+ "                ctx._source.string.putAll(params.string);"
					+ "            }"
					+ "        }"
					+ "        if (params.text != null) {"
					+ "            if (ctx._source.text == null) {"
					+ "                ctx._source.text = params.text;"
					+ "            }"
					+ "            else {"
					+ "                ctx._source.text.putAll(params.text);"
					+ "            }"
					+ "        }"
					+ "        if (params.metric != null) {"
					+ "            if (ctx._source.metric == null) {"
					+ "                ctx._source.metric = params.metric;"
					+ "            }"
					+ "            else {"
					+ "                ctx._source.metric.putAll(params.metric);"
					+ "            }"
					+ "        }"
					+ "        if (params.logi != null) {"
					+ "            if (ctx._source.log == null) {"
					+ "                ctx._source.log = params.logi;"
					+ "            } else {"
					+ "                ctx._source.log += '\\n' + params.logi;"
					+ "            }"
					+ "        }"
					+ "        if (params.name != null) {"
					+ "            ctx._source.name = params.name;"
					+ "        }"
					+ "        if (params.parentId != null) {"
					+ "            ctx._source.parentId = params.parentId;"
					+ "        }"
					+ "        if (params.primaryId != null) {"
					+ "            ctx._source.primaryId = params.primaryId;"
					+ "        }"
					+ "        if (params.primary != null && ctx._source.primary == null) {"
					+ "            ctx._source.primary = params.primary;"
					+ "        }"
					+ "        if (params.parentsPath != null) {"
					+ "            ctx._source.parentsPath = params.parentsPath;"
					+ "        }"
					+ "        if (params.orphan != null && params.orphan) {"
					+ "            ctx._source.orphan = true;"
					+ "        }\"\n";
}

