/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the LGPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at  http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jn.langx.configuration.file.directoryfile;

import com.jn.langx.configuration.AbstractConfigurationRepository;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.diff.MapDiffResult;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.io.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

/**
 * multiple configuration file in one directory, every configuration will be load as a configurationlangx
 *
 * @param <T>
 */
public class DirectoryBasedFileConfigurationRepository<T extends Configuration> extends AbstractConfigurationRepository<T, DirectoryBasedFileConfigurationLoader<T>, DirectoryBasedFileConfigurationWriter<T>> {
    private static final Logger logger = LoggerFactory.getLogger(DirectoryBasedFileConfigurationRepository.class);


    private String directory;

    private Map<String, Long> lastModifiedTimeMap = Collects.emptyHashMap();


    public void setDirectory(String directory) {
        this.directory = directory;
    }


    @Override
    public void init() throws InitializationException {
        if (!inited) {
            super.init();
            Preconditions.checkNotNull(loader, "the configuration load is null");
            Preconditions.checkTrue(Strings.isNotBlank(directory), "directory is null");
            if (!Files.exists(new File(directory))) {
                logger.warn("Can't find a directory : {}, will create it", new File(directory).getAbsoluteFile());
                Files.makeDirs(directory);
            }
            loader.setDirectory(directory);

            if (writer == null) {
                logger.warn("The writer is not specified for the repository ({}), will disable write configuration to storage", name);
            } else {
                writer.setDirectory(directory);
            }
            // enable refresh
            if (reloadIntervalInSeconds > 1) {
                logger.info("The configuration refresh task is disabled for repository: {}", name);
            }
            inited = true;

        }
    }

    public void reload() {
        Map<String, Long> modifiedTimeMap = loader.scanConfigurationFileModifiedTimes();
        try {
            MapDiffResult<String, Long> lastModifiedDiffResult = Collects.diff(lastModifiedTimeMap, modifiedTimeMap);
            Collects.forEach(lastModifiedDiffResult.getRemoves(), new Consumer2<String, Long>() {
                @Override
                public void accept(String id, Long lastModified) {
                    removeById(id, false);
                }
            });
            Collects.forEach(lastModifiedDiffResult.getUpdates(), new Consumer2<String, Long>() {
                @Override
                public void accept(String id, Long lastModified) {
                    T configurationInStorage = loader.load(id);
                    T configurationInCache = getById(id);
                    if (configurationInCache == null) {
                        add(configurationInStorage, false);
                    } else if (!configurationInCache.equals(configurationInStorage)) {
                        update(configurationInStorage, false);
                    }
                }
            });
            Collects.forEach(lastModifiedDiffResult.getAdds(), new Consumer2<String, Long>() {
                @Override
                public void accept(String id, Long lastModified) {
                    T configurationInStorage = loader.load(id);
                    T configurationInCache = getById(id);
                    if (configurationInCache == null) {
                        add(configurationInStorage, false);
                    } else if (!configurationInCache.equals(configurationInStorage)) {
                        update(configurationInStorage, false);
                    }
                }
            });
        } finally {
            lastModifiedTimeMap = modifiedTimeMap;
        }
    }

}
