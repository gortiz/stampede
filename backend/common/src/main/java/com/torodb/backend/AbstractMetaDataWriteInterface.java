/*
 *     This file is part of ToroDB.
 *
 *     ToroDB is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     ToroDB is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with ToroDB. If not, see <http://www.gnu.org/licenses/>.
 *
 *     Copyright (c) 2014, 8Kdata Technology
 *     
 */

package com.torodb.backend;

import javax.inject.Singleton;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.TableField;
import org.jooq.conf.ParamType;

import com.torodb.backend.ErrorHandler.Context;
import com.torodb.backend.converters.TableRefConverter;
import com.torodb.backend.tables.MetaCollectionTable;
import com.torodb.backend.tables.MetaDatabaseTable;
import com.torodb.backend.tables.MetaDocPartIndexTable;
import com.torodb.backend.tables.MetaDocPartTable;
import com.torodb.backend.tables.MetaFieldIndexTable;
import com.torodb.backend.tables.MetaFieldTable;
import com.torodb.backend.tables.MetaIndexFieldTable;
import com.torodb.backend.tables.MetaIndexTable;
import com.torodb.backend.tables.MetaScalarTable;
import com.torodb.core.TableRef;
import com.torodb.core.transaction.metainf.FieldIndexOrdering;
import com.torodb.core.transaction.metainf.FieldType;
import com.torodb.core.transaction.metainf.MetaCollection;
import com.torodb.core.transaction.metainf.MetaDatabase;
import com.torodb.core.transaction.metainf.MetaDocPart;
import com.torodb.core.transaction.metainf.MetaDocPartIndex;
import com.torodb.core.transaction.metainf.MetaField;
import com.torodb.core.transaction.metainf.MetaFieldIndex;
import com.torodb.core.transaction.metainf.MetaIndex;
import com.torodb.core.transaction.metainf.MetaIndexField;
import com.torodb.core.transaction.metainf.MetaScalar;

/**
 *
 */
@Singleton
public abstract class AbstractMetaDataWriteInterface implements MetaDataWriteInterface {

    private final MetaDatabaseTable<?> metaDatabaseTable;
    private final MetaCollectionTable<?> metaCollectionTable;
    private final MetaDocPartTable<?, ?> metaDocPartTable;
    private final MetaFieldTable<?, ?> metaFieldTable;
    private final MetaScalarTable<?, ?> metaScalarTable;
    private final MetaIndexTable<?> metaIndexTable;
    private final MetaIndexFieldTable<?, ?> metaIndexFieldTable;
    private final MetaDocPartIndexTable<?, ?> metaDocPartIndexTable;
    private final MetaFieldIndexTable<?, ?> metaFieldIndexTable;
    private final SqlHelper sqlHelper;
    
    public AbstractMetaDataWriteInterface(MetaDataReadInterface derbyMetaDataReadInterface, 
            SqlHelper sqlHelper) {
        this.metaDatabaseTable = derbyMetaDataReadInterface.getMetaDatabaseTable();
        this.metaCollectionTable = derbyMetaDataReadInterface.getMetaCollectionTable();
        this.metaDocPartTable = derbyMetaDataReadInterface.getMetaDocPartTable();
        this.metaFieldTable = derbyMetaDataReadInterface.getMetaFieldTable();
        this.metaScalarTable = derbyMetaDataReadInterface.getMetaScalarTable();
        this.metaIndexTable = derbyMetaDataReadInterface.getMetaIndexTable();
        this.metaIndexFieldTable = derbyMetaDataReadInterface.getMetaIndexFieldTable();
        this.metaDocPartIndexTable = derbyMetaDataReadInterface.getMetaDocPartIndexTable();
        this.metaFieldIndexTable = derbyMetaDataReadInterface.getMetaFieldIndexTable();
        this.sqlHelper = sqlHelper;
    }

    @Override
    public void createMetaDatabaseTable(DSLContext dsl) {
    	String schemaName = metaDatabaseTable.getSchema().getName();
    	String tableName = metaDatabaseTable.getName();
        String statement = getCreateMetaDatabaseTableStatement(schemaName, tableName);
        sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaDatabaseTableStatement(String schemaName, String tableName);
    
    @Override
    public void createMetaCollectionTable(DSLContext dsl) {
    	String schemaName = metaCollectionTable.getSchema().getName();
    	String tableName = metaCollectionTable.getName();
    	String statement = getCreateMetaCollectionTableStatement(schemaName, tableName);
    	sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaCollectionTableStatement(String schemaName, String tableName);
    
    @Override
    public void createMetaDocPartTable(DSLContext dsl) {
    	String schemaName = metaDocPartTable.getSchema().getName();
    	String tableName = metaDocPartTable.getName();
    	String statement = getCreateMetaDocPartTableStatement(schemaName, tableName);
    	sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaDocPartTableStatement(String schemaName, String tableName);

    @Override
    public void createMetaFieldTable(DSLContext dsl) {
    	String schemaName = metaFieldTable.getSchema().getName();
    	String tableName = metaFieldTable.getName();
    	String statement = getCreateMetaFieldTableStatement(schemaName, tableName);
    	sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaFieldTableStatement(String schemaName, String tableName);
    
    @Override
    public void createMetaScalarTable(DSLContext dsl) {
        String schemaName = metaScalarTable.getSchema().getName();
        String tableName = metaScalarTable.getName();
        String statement = getCreateMetaScalarTableStatement(schemaName, tableName);
        sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaScalarTableStatement(String schemaName, String tableName);

	@Override
    public void createMetaIndexTable(DSLContext dsl) {
        String schemaName = metaIndexTable.getSchema().getName();
        String tableName = metaIndexTable.getName();
        String statement = getCreateMetaIndexTableStatement(schemaName, tableName);
        sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaIndexTableStatement(String schemaName, String tableName);

    @Override
    public void createMetaIndexFieldTable(DSLContext dsl) {
        String schemaName = metaIndexFieldTable.getSchema().getName();
        String tableName = metaIndexFieldTable.getName();
        String statement = getCreateMetaIndexFieldTableStatement(schemaName, tableName);
        sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaIndexFieldTableStatement(String schemaName, String tableName);

    @Override
    public void createMetaDocPartIndexTable(DSLContext dsl) {
        String schemaName = metaDocPartIndexTable.getSchema().getName();
        String tableName = metaDocPartIndexTable.getName();
        String statement = getCreateMetaDocPartIndexTableStatement(schemaName, tableName);
        sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaDocPartIndexTableStatement(String schemaName, String tableName);

    @Override
    public void createMetaFieldIndexTable(DSLContext dsl) {
        String schemaName = metaFieldIndexTable.getSchema().getName();
        String tableName = metaFieldIndexTable.getName();
        String statement = getCreateMetaFieldIndexTableStatement(schemaName, tableName);
        sqlHelper.executeStatement(dsl, statement, Context.CREATE_TABLE);
    }

    protected abstract String getCreateMetaFieldIndexTableStatement(String schemaName, String tableName);

    @Override
	public void addMetaDatabase(DSLContext dsl, MetaDatabase database) {
        String statement = getAddMetaDatabaseStatement(database.getName(), database.getIdentifier());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
	}

    @Override
    public void addMetaCollection(DSLContext dsl, MetaDatabase database, MetaCollection collection) {
        String statement = getAddMetaCollectionStatement(database.getName(), collection.getName(), collection.getIdentifier());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
    }

    @Override
    public void addMetaDocPart(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
            MetaDocPart docPart) {
        String statement = getAddMetaDocPartStatement(database.getName(), collection.getName(), docPart.getTableRef(), 
                docPart.getIdentifier());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
    }

	@Override
	public void addMetaField(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
	        MetaDocPart docPart, MetaField field) {
	    String statement = getAddMetaFieldStatement(database.getName(), collection.getName(), docPart.getTableRef(), 
	            field.getName(), field.getIdentifier(),
                field.getType());
		sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
	}

	@Override
	public void addMetaScalar(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
	        MetaDocPart docPart, MetaScalar scalar) {
	    String statement = getAddMetaScalarStatement(database.getName(), collection.getName(), docPart.getTableRef(), 
	            scalar.getIdentifier(), scalar.getType());
		sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
	}

    @Override
    public void addMetaIndex(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
            MetaIndex index) {
        String statement = getAddMetaIndexStatement(database.getName(), collection.getName(), index.getName(), 
                index.isUnique());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
    }

    @Override
    public void addMetaIndexField(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
            MetaIndex index, MetaIndexField field) {
        String statement = getAddMetaIndexFieldStatement(database.getName(), collection.getName(), index.getName(), 
                field.getPosition(), field.getTableRef(), field.getName(), field.getOrdering());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
    }

    @Override
    public void addMetaDocPartIndex(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
            MetaDocPart docPart, MetaDocPartIndex index) {
        String statement = getAddMetaDocPartIndexStatement(database.getName(), index.getIdentifier(), collection.getName(), 
                docPart.getTableRef(), index.isUnique());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
    }

    @Override
    public void addMetaFieldIndex(DSLContext dsl, MetaDatabase database, MetaCollection collection, 
            MetaDocPart docPart, MetaDocPartIndex index, MetaFieldIndex field) {
        String statement = getAddMetaFieldIndexStatement(database.getName(), index.getIdentifier(), field.getPosition(), 
                collection.getName(), docPart.getTableRef(), field.getName(), field.getType(), field.getOrdering());
        sqlHelper.executeUpdate(dsl, statement, Context.META_INSERT);
    }

    protected String getAddMetaDatabaseStatement(String databaseName, String databaseIdentifier) {
        String statement = sqlHelper.dsl().insertInto(metaDatabaseTable)
            .set(metaDatabaseTable.newRecord().values(databaseName, databaseIdentifier)).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getAddMetaCollectionStatement(String databaseName, String collectionName,
            String collectionIdentifier) {
        String statement = sqlHelper.dsl().insertInto(metaCollectionTable)
            .set(metaCollectionTable.newRecord()
            .values(databaseName, collectionName, collectionIdentifier)).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getAddMetaDocPartStatement(String databaseName, String collectionName, TableRef tableRef,
            String docPartIdentifier) {
        String statement = sqlHelper.dsl().insertInto(metaDocPartTable)
            .set(metaDocPartTable.newRecord()
            .values(databaseName, collectionName, tableRef, docPartIdentifier)).getSQL(ParamType.INLINED);
        return statement;
    }
    
    protected String getAddMetaFieldStatement(String databaseName, String collectionName, TableRef tableRef,
            String fieldName, String fieldIdentifier, FieldType type) {
        String statement = sqlHelper.dsl().insertInto(metaFieldTable)
                .set(metaFieldTable.newRecord()
                .values(databaseName, collectionName, tableRef, fieldName, type, fieldIdentifier)).getSQL(ParamType.INLINED);
        return statement;
    }
    
    protected String getAddMetaScalarStatement(String databaseName, String collectionName, TableRef tableRef,
            String fieldIdentifier, FieldType type) {
        String statement = sqlHelper.dsl().insertInto(metaScalarTable)
                .set(metaScalarTable.newRecord()
                .values(databaseName, collectionName, tableRef, type, fieldIdentifier)).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getAddMetaIndexStatement(String databaseName, String collectionName, 
            String indexName, boolean unique) {
        String statement = sqlHelper.dsl().insertInto(metaIndexTable)
            .set(metaIndexTable.newRecord()
            .values(databaseName, collectionName, indexName, unique)).getSQL(ParamType.INLINED);
        return statement;
    }
    
    protected String getAddMetaIndexFieldStatement(String databaseName, String collectionName, String indexName, 
            int position, TableRef tableRef, String fieldName, FieldIndexOrdering ordering) {
        String statement = sqlHelper.dsl().insertInto(metaIndexFieldTable)
                .set(metaIndexFieldTable.newRecord()
                .values(databaseName, collectionName, indexName, position, tableRef, fieldName, ordering)).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getAddMetaDocPartIndexStatement(String databaseName, String collectionName, String indexName, 
            TableRef tableRef, boolean unique) {
        String statement = sqlHelper.dsl().insertInto(metaDocPartIndexTable)
            .set(metaDocPartIndexTable.newRecord()
            .values(databaseName, indexName, collectionName, tableRef, unique)).getSQL(ParamType.INLINED);
        return statement;
    }
    
    protected String getAddMetaFieldIndexStatement(String databaseName, String indexName, int position, String collectionName,  
            TableRef tableRef, String fieldName, FieldType fieldType, FieldIndexOrdering ordering) {
        String statement = sqlHelper.dsl().insertInto(metaFieldIndexTable)
                .set(metaFieldIndexTable.newRecord()
                .values(databaseName, indexName, position, collectionName, tableRef, fieldName, fieldType, ordering)).getSQL(ParamType.INLINED);
        return statement;
    }

    @Override
    public void deleteMetaDatabase(DSLContext dsl, MetaDatabase database) {
        String statement = getDeleteMetaDatabaseStatement(database.getName());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    @Override
    public void deleteMetaCollection(DSLContext dsl, MetaDatabase database, MetaCollection collection) {
        String statement = getDeleteMetaCollectionStatement(database.getName(), collection.getName());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    @Override
    public void deleteMetaDocPart(DSLContext dsl, MetaDatabase database, MetaCollection collection, MetaDocPart docPart) {
        String statement = getDeleteMetaDocPartStatement(database.getName(), collection.getName(), docPart.getTableRef());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    @Override
    public void deleteMetaField(DSLContext dsl, MetaDatabase database, MetaCollection collection, MetaDocPart docPart, MetaField field) {
        String statement = getDeleteMetaFieldStatement(database.getName(), collection.getName(), docPart.getTableRef(), field.getName(),
                field.getType());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    @Override
    public void deleteMetaScalar(DSLContext dsl, MetaDatabase database, MetaCollection collection, MetaDocPart docPart, MetaScalar scalar) {
        String statement = getDeleteMetaScalarStatement(database.getName(), collection.getName(), docPart.getTableRef(), scalar.getType());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    @Override
    public void deleteMetaIndex(DSLContext dsl, MetaDatabase database, MetaCollection collection, MetaIndex index) {
        String statement = getDeleteMetaIndexFieldStatement(database.getName(), collection.getName(), index.getName());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
        statement = getDeleteMetaIndexStatement(database.getName(), collection.getName(), index.getName());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    @Override
    public void deleteMetaDocPartIndex(DSLContext dsl, MetaDatabase database, MetaCollection collection, MetaDocPart docPart, MetaDocPartIndex index) {
        String statement = getDeleteMetaFieldIndexStatement(database.getName(), collection.getName(), index.getIdentifier());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
        statement = getDeleteMetaDocPartIndexStatement(database.getName(), collection.getName(), index.getIdentifier());
        sqlHelper.executeUpdate(dsl, statement, Context.META_DELETE);
    }

    protected String getDeleteMetaDatabaseStatement(String databaseName) {
        String statement = sqlHelper.dsl().deleteFrom(metaDatabaseTable)
                .where(metaDatabaseTable.NAME.eq(databaseName)).getSQL(ParamType.INLINED);
            return statement;
    }

    protected String getDeleteMetaCollectionStatement(String databaseName, String collectionName) {
        String statement = sqlHelper.dsl().deleteFrom(metaCollectionTable)
                .where(metaCollectionTable.DATABASE.eq(databaseName)
                    .and(metaCollectionTable.NAME.eq(collectionName))).getSQL(ParamType.INLINED);
            return statement;
    }

    protected String getDeleteMetaDocPartStatement(String databaseName, String collectionName, TableRef tableRef) {
        String statement = sqlHelper.dsl().deleteFrom(metaDocPartTable)
            .where(metaDocPartTable.DATABASE.eq(databaseName)
                    .and(metaDocPartTable.COLLECTION.eq(collectionName))
                    .and(getTableRefEqCondition(metaDocPartTable.TABLE_REF, tableRef))).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getDeleteMetaIndexStatement(String databaseName, String collectionName, String indexName) {
        String statement = sqlHelper.dsl().deleteFrom(metaIndexTable)
            .where(metaIndexTable.DATABASE.eq(databaseName)
                    .and(metaIndexTable.COLLECTION.eq(collectionName))
                    .and(metaIndexTable.NAME.eq(indexName))).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getDeleteMetaIndexFieldStatement(String databaseName, String collectionName, String indexName) {
        String statement = sqlHelper.dsl().deleteFrom(metaIndexFieldTable)
            .where(metaIndexFieldTable.DATABASE.eq(databaseName)
                    .and(metaIndexFieldTable.COLLECTION.eq(collectionName))
                    .and(metaIndexFieldTable.NAME.eq(indexName))).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getDeleteMetaDocPartIndexStatement(String databaseName, String collectionName, String indexIdentifier) {
        String statement = sqlHelper.dsl().deleteFrom(metaDocPartIndexTable)
            .where(metaDocPartIndexTable.DATABASE.eq(databaseName)
                    .and(metaDocPartIndexTable.IDENTIFIER.eq(indexIdentifier))).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getDeleteMetaFieldIndexStatement(String databaseName, String collectionName, String indexIdentifier) {
        String statement = sqlHelper.dsl().deleteFrom(metaFieldIndexTable)
            .where(metaFieldIndexTable.DATABASE.eq(databaseName)
                    .and(metaFieldIndexTable.IDENTIFIER.eq(indexIdentifier))).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getDeleteMetaFieldStatement(String databaseName, String collectionName, TableRef tableRef,
            String fieldName, FieldType type) {
        String statement = sqlHelper.dsl().deleteFrom(metaFieldTable)
                .where(metaFieldTable.DATABASE.eq(databaseName)
                        .and(metaFieldTable.COLLECTION.eq(collectionName))
                        .and(getTableRefEqCondition(metaFieldTable.TABLE_REF, tableRef))
                        .and(metaFieldTable.NAME.eq(fieldName))
                        .and(metaFieldTable.TYPE.eq(type))).getSQL(ParamType.INLINED);
        return statement;
    }

    protected String getDeleteMetaScalarStatement(String databaseName, String collectionName, TableRef tableRef,
            FieldType type) {
        String statement = sqlHelper.dsl().deleteFrom(metaScalarTable)
                .where(metaScalarTable.DATABASE.eq(databaseName)
                        .and(metaScalarTable.COLLECTION.eq(collectionName))
                        .and(getTableRefEqCondition(metaScalarTable.TABLE_REF, tableRef))
                        .and(metaScalarTable.TYPE.eq(type))).getSQL(ParamType.INLINED);
        return statement;
    }

    @Override
    public int consumeRids(DSLContext dsl, MetaDatabase database, MetaCollection collection, MetaDocPart docPart, int count) {
        Record1<Integer> lastRid = dsl.select(metaDocPartTable.LAST_RID).from(metaDocPartTable).where(
                metaDocPartTable.DATABASE.eq(database.getIdentifier())
                .and(metaDocPartTable.COLLECTION.eq(collection.getIdentifier()))
                .and(getTableRefEqCondition(metaDocPartTable.TABLE_REF, docPart.getTableRef())))
            .fetchOne();
        dsl.update(metaDocPartTable).set(metaDocPartTable.LAST_RID, metaDocPartTable.LAST_RID.plus(count)).where(
                metaDocPartTable.DATABASE.eq(database.getIdentifier())
                .and(metaDocPartTable.COLLECTION.eq(collection.getIdentifier()))
                .and(getTableRefEqCondition(metaDocPartTable.TABLE_REF, docPart.getTableRef())))
            .execute();
        return lastRid.value1();
    }
    
    protected abstract Condition getTableRefEqCondition(TableField<?, ?> field, TableRef tableRef);
}
