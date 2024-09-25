package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Encounter
 * @date 2024/09/25 14:03<br/>
 * 自定义定时任务类
 * <pre>
 * cron: Cron表达式不涉及单位，它是一个字符串，用于定义任务执行的时间表。
 *
 * fixedRate: 此属性的单位是毫秒（ms）。它指定了任务执行的频率，即任务执行完毕后，需要等待多长时间才能再次执行。
 *
 * fixedDelay: 此属性的单位也是毫秒（ms）。它指定了任务执行的延迟，即任务执行完毕后，需要等待多长时间才能再次执行。
 *
 * initialDelay: 此属性的单位同样是毫秒（ms）。它指定了首次任务执行之前的延迟时间。
 *
 * zone: 此属性不涉及单位，它是一个字符串，用于指定时区。
 * </pre>
 */
@Component
@Slf4j
public class MyTask
    {
        /**
         * 执行任务
         * 每隔5s运行一次
         */
        //@Scheduled(fixedRate = 5000)
        //@Scheduled(initialDelay = 5000, fixedDelay = 5000)
        //@Scheduled(cron = "0/5 * * * * ?")
        public void executeTask()
            {
                log.info("MyTask executed:{}", new Date());
            }
        
    }
