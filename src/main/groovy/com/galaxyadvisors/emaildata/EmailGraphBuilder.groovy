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

public class EmailGraphBuilder {
    protected final static Logger logger = LoggerFactory.getLogger(EmailGraphBuilder.class)

    protected InputStream is
    protected EntityFacadeImpl efi
    protected ServiceFacadeImpl sfi
    protected ExecutionContext executionContext
    protected ExecutionContextImpl eci

    EmailGraphBuilder(ExecutionContextImpl eci, EntityFacadeImpl efi) {
        this.efi = efi
        this.fi.setFieldName(fi.getName())
        this.sfi = efi.getEcfi().getServiceFacade()
        this.eci = eci
    }

    EmailGraphBuilder(ExecutionContextImpl eci, EntityFacadeImpl efi) {
        this.efi = efi
        this.sfi = efi.getEcfi().getServiceFacade()
        this.eci = eci
    }

    String get() {
        return ""
    }

}
