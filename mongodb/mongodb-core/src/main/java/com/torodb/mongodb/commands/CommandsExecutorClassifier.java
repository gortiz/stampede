/*
 * This file is part of ToroDB.
 *
 * ToroDB is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ToroDB is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mongodb-core. If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2016 8Kdata.
 * 
 */

package com.torodb.mongodb.commands;

import java.util.Map;

import javax.inject.Inject;

import com.eightkdata.mongowp.server.api.Command;
import com.eightkdata.mongowp.server.api.CommandImplementation;
import com.eightkdata.mongowp.server.api.CommandsExecutor;
import com.eightkdata.mongowp.server.api.impl.MapBasedCommandsExecutor;
import com.google.common.collect.ImmutableMap;
import com.torodb.mongodb.commands.impl.NotImplementedCommandImplementation;
import com.torodb.mongodb.core.ExclusiveWriteMongodTransaction;
import com.torodb.mongodb.core.MongodConnection;
import com.torodb.mongodb.core.MongodTransaction;
import com.torodb.mongodb.core.ReadOnlyMongodTransaction;
import com.torodb.mongodb.core.WriteMongodTransaction;

/**
 *
 */
public class CommandsExecutorClassifier {
    private final CommandsExecutor<ExclusiveWriteMongodTransaction> exclusiveWriteCommandsExecutor;
    private final CommandsExecutor<WriteMongodTransaction> writeCommandsExecutor;
    private final CommandsExecutor<ReadOnlyMongodTransaction> readOnlyCommandsExecutor;
    private final CommandsExecutor<MongodConnection> connectionCommandsExecutor;

    @Inject
    public CommandsExecutorClassifier(
            ExclusiveWriteTransactionImplementations exclusiveWriteTransImpls,
            WriteTransactionImplementations writeTransImpls,
            GeneralTransactionImplementations generalTransImpls,
            ConnectionCommandsExecutor connTransImpls) {
        ImmutableMap.Builder<Command<?,?>, CommandImplementation<?, ?, ? super ExclusiveWriteMongodTransaction>> exclusiveWriteMapBuilder =
                ImmutableMap.builder();
        ImmutableMap.Builder<Command<?,?>, CommandImplementation<?, ?, ? super WriteMongodTransaction>> writeMapBuilder =
                ImmutableMap.builder();
        ImmutableMap.Builder<Command<?,?>, CommandImplementation<?, ?, ? super ReadOnlyMongodTransaction>> readOnlyMapBuilder =
                ImmutableMap.builder();

        generalTransImpls.getMap().entrySet().stream()
                .filter(CommandsExecutorClassifier::isImplemented)
                .forEach((entry) -> {
                    CommandImplementation<?, ?, ? super MongodTransaction> value = entry.getValue();
                    writeMapBuilder.put(entry.getKey(), (CommandImplementation<?, ?, ? super WriteMongodTransaction>) value);
                    readOnlyMapBuilder.put(entry.getKey(), (CommandImplementation<?, ?, ? super ReadOnlyMongodTransaction>) value);
                });

        writeTransImpls.getMap().entrySet().stream()
                .filter(CommandsExecutorClassifier::isImplemented)
                .forEach(entry -> writeMapBuilder.put(entry));

        exclusiveWriteTransImpls.getMap().entrySet().stream()
                .filter(CommandsExecutorClassifier::isImplemented)
                .forEach(entry -> exclusiveWriteMapBuilder.put(entry));

        this.exclusiveWriteCommandsExecutor = MapBasedCommandsExecutor.fromMap(exclusiveWriteMapBuilder.build());
        this.writeCommandsExecutor = MapBasedCommandsExecutor.fromMap(writeMapBuilder.build());
        this.readOnlyCommandsExecutor = MapBasedCommandsExecutor.fromMap(readOnlyMapBuilder.build());
        this.connectionCommandsExecutor = MapBasedCommandsExecutor.fromMap(connTransImpls.getMap());
    }

    public CommandsExecutor<? super ExclusiveWriteMongodTransaction> getExclusiveWriteCommandsExecutor() {
        return exclusiveWriteCommandsExecutor;
    }

    public CommandsExecutor<? super WriteMongodTransaction> getWriteCommandsExecutor() {
        return writeCommandsExecutor;
    }

    public CommandsExecutor<? super ReadOnlyMongodTransaction> getReadOnlyCommandsExecutor() {
        return readOnlyCommandsExecutor;
    }

    public CommandsExecutor<? super MongodConnection> getConnectionCommandsExecutor() {
        return connectionCommandsExecutor;
    }

    private static <E> boolean isImplemented(Map.Entry<Command<?, ?>, CommandImplementation<?, ?, ? super E>> entry) {
        return !(entry.getValue() instanceof NotImplementedCommandImplementation);
    }

}
