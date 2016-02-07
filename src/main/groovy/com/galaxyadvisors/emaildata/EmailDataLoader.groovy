package com.galaxyadvisors.emaildata

import groovy.transform.CompileStatic
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import org.moqui.BaseException
import org.moqui.context.ArtifactTarpitException
import org.moqui.context.ExecutionContext
import org.moqui.impl.context.ExecutionContextImpl
import org.moqui.impl.entity.EntityValueImpl
import org.moqui.impl.service.ServiceCallSyncImpl
import org.moqui.impl.service.ServiceFacadeImpl

import java.sql.Timestamp

import org.moqui.context.ArtifactAuthorizationException
import org.moqui.context.ArtifactExecutionFacade
import org.moqui.context.ArtifactExecutionInfo
import org.moqui.context.Cache
import org.moqui.entity.EntityList
import org.moqui.entity.EntityFind
import org.moqui.entity.EntityCondition.ComparisonOperator
import org.moqui.entity.EntityValue
import org.moqui.impl.entity.EntityDefinition
import org.moqui.impl.entity.EntityFacadeImpl
import org.moqui.entity.EntityCondition
import org.apache.commons.fileupload.FileItem
import com.tinkerpop.gremlin.groovy.Gremlin

import org.slf4j.Logger
import org.slf4j.LoggerFactory

public class EmailDataLoader {
    protected final static Logger logger = LoggerFactory.getLogger(EmailDataLoader.class)

    protected InputStream is
    protected EntityFacadeImpl efi
    protected ServiceFacadeImpl sfi
    protected String location
    protected char csvDelimiter = ','
    protected char csvCommentStart = '#'
    protected char csvQuoteChar = '"'
    protected FileItem fi
    protected ExecutionContext executionContext
    protected ExecutionContextImpl eci

    EmailDataLoader(ExecutionContextImpl eci, EntityFacadeImpl efi, FileItem fi) {
        this.efi = efi
        this.fi = fi
        this.fi.setFieldName(fi.getName())
        this.is = fi.getInputStream()
        this.location = fi.getName()
        this.sfi = efi.getEcfi().getServiceFacade()
        this.eci = eci
    }

    EmailDataLoader(ExecutionContextImpl eci, EntityFacadeImpl efi, String fileName, InputStream is) {
        this.efi = efi
        this.is = is
        this.location = fileName
        this.sfi = efi.getEcfi().getServiceFacade()
        this.eci = eci
    }

    boolean isLoaded() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))

		CSVParser parser = CSVFormat.newFormat(csvDelimiter)
				.withCommentMarker(csvCommentStart)
				.withQuote(csvQuoteChar)
				.withSkipHeaderRecord(true) // TODO: remove this? does it even do anything?
				.withIgnoreEmptyLines(true)
				.withIgnoreSurroundingSpaces(true)
				.parse(reader)

		Iterator<CSVRecord> iterator = parser.iterator()

		if (!iterator.hasNext()) throw new BaseException("Not loading file, no data found")

		CSVRecord firstLineRecord = iterator.next()
		String entityName = firstLineRecord.get(0)
		boolean isService
		if (efi.isEntityDefined(entityName)) {
			isService = false
		} else if (sfi.isServiceDefined(entityName)) {
			isService = true
		} else {
			throw new BaseException("CSV first line first field [${entityName}] is not a valid entity name or service name")
		}

		if (firstLineRecord.size() > 1) {
			// second field is data type
			String type = firstLineRecord.get(1)
			//if (type && edli.dataTypes && !edli.dataTypes.contains(type)) {
			//	if (logger.isInfoEnabled()) logger.info("Skipping file [${location}], is a type to skip (${type})")
			//	return false
			//}
		}

		if (!iterator.hasNext()) throw new BaseException("Not loading file [${location}], no second (header) line found")
		CSVRecord headerRecord = iterator.next()
		Map<String, Integer> headerMap = [:]
		for (int i = 0; i < headerRecord.size(); i++) headerMap.put(headerRecord.get(i), i)

		// logger.warn("======== CSV entity/service [${entityName}] headerMap: ${headerMap}")
		while (iterator.hasNext()) {
            CSVRecord record = iterator.next()
            // logger.warn("======== CSV record: ${record.toString()}")
            if (isService) {
                ServiceCallSyncImpl currentScs = (ServiceCallSyncImpl) sfi.sync().name(entityName)
                for (Map.Entry<String, Integer> header in headerMap)
                    currentScs.parameter(header.key, record.get((int) header.value))
//				valueHandler.handleService(currentScs)
//				valuesRead++
            } else {
                Map<String, Object> im = new HashMap<String, Object>()
                for (Map.Entry<String, Integer> header in headerMap)
                    im.put(header.key, record.get((int) header.value))
                eci.service.async().name("create", entityName).parameters(im).call()
            }

        }
        true
    }
}
