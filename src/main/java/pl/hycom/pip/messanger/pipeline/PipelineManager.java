/**
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.hycom.pip.messanger.pipeline;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import pl.hycom.pip.messanger.pipeline.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class PipelineManager implements ApplicationContextAware, InitializingBean {

    private ApplicationContext applicationContext;

    private Pipeline pipeline;

    private String pipelineFileURL;

    public PipelineManager(String pipelineFileURL) {
        this.pipelineFileURL = pipelineFileURL;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(getClass().getResourceAsStream(pipelineFileURL)));

        pipeline = (Pipeline) JAXBContext.newInstance(Pipeline.class).createUnmarshaller().unmarshal(xsr);
    }

    private void runProcessForChain(PipelineContext context, PipelineChain pipelineChain) throws PipelineException {
        runProcessForLink(context, pipelineChain, pipelineChain.getHeadLink());
    }

    private void runProcessForLink(PipelineContext context, PipelineChain pipelineChain, String name) throws PipelineException {
        if (StringUtils.isEmpty(name)) {
            return;
        }

        // find link
        PipelineLink link = findPipelineLink(name, pipelineChain);

        // run process
        Processor processor = link.getProcessor();
        PipelineProcessor pipelineProcessor = applicationContext.getBean(processor.getBean(), PipelineProcessor.class);

        if (pipelineProcessor == null) {
            throw new PipelineException("Bean [" + processor.getBean() + "] is null");
        }

        log.debug("Started process of pipelineLink with name[" + link.getName() + "]");
        Integer processResult = pipelineProcessor.runProcess(context);
        log.debug("Actual result of pipelineLink[" + link.getName() + "] is: " + processResult);
        List<Transition> transitions = Optional.ofNullable(link.getTransitions()).orElse(Collections.emptyList()).parallelStream()
                .filter(t -> StringUtils.equals(t.getReturnValue(), processResult.toString())).collect(Collectors.toList());

        // start for transitions
        for (Transition transition : transitions) {
            log.debug("Started transition with link[" + transition.getLink() + "] and expected result: " + transition.getReturnValue());
            runProcessForLink(context, pipelineChain, transition.getLink());
        }
    }

    public void runProcess(String pipelineChainName, Map<String, Object> params) throws PipelineException {
        log.debug("Starting pipeline[" + pipelineChainName + "] with params[" + params + "]");
        PipelineContext ctx = new PipelineContext(params);
        PipelineChain pipelineChain = findPipelineChain(pipelineChainName);
        runProcessForChain(ctx, pipelineChain);
    }

    private PipelineChain findPipelineChain(String pipelineChainName) throws PipelineException {
        log.debug("Started finding pipelineChain with name[" + pipelineChainName + "]");
        for (PipelineChain pc : pipeline.getPipelineChains()) {
            if (StringUtils.equals(pc.getName(), pipelineChainName)) {
                log.debug("Found pipelineLink with name[" + pipelineChainName + "]");
                return pc;
            }
        }

        throw new PipelineException("No pipelineChain with name[" + pipelineChainName + "]");
    }

    private PipelineLink findPipelineLink(String pipelineLinkName, PipelineChain pipelineChain) throws PipelineException {
        log.debug("Started finding pipelineLink with name[" + pipelineLinkName + "] in pipelineChain with name[" + pipelineChain.getName() + "]");

        for (PipelineLink pl : pipelineChain.getPipelineLinks()) {
            if (StringUtils.equals(pl.getName(), pipelineLinkName)) {
                log.debug("Found pipelineLink with name[" + pipelineLinkName + "] in pipelineChain with name[" + pipelineChain.getName() + "]");
                return pl;
            }
        }

        throw new PipelineException("No pipelineLink with name[" + pipelineLinkName + "] in pipelineChain with name[" + pipelineChain.getName() + "]");
    }
}
