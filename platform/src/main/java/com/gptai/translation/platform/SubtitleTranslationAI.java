package com.gptai.translation.platform;

import com.gptai.translation.common.exceptions.UtilException;
import com.gptai.translation.common.utils.SpringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootApplication(scanBasePackages = "com.gptai.translation")
public class SubtitleTranslationAI {

    public static void main(String[] args) {
        SpringUtil.loadEnvFile(args);
        SpringApplication.run(SubtitleTranslationAI.class, args);
    }

    public static void restartApp() {
        try {
            var executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(SubtitleTranslationAI::internalRestartApp, 1, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new UtilException("Failed to restart the application.", e);
        }
    }

    private synchronized static void internalRestartApp() {
        var args = SpringUtil.getBean(ApplicationArguments.class);
        new Thread(() -> {
            try {
                TimeUnit.MICROSECONDS.sleep(500);
            } catch (InterruptedException e) {
                throw new UtilException("Execute needed sleep is interrupted!", e);
            }
            try {
                SpringApplication.exit(SpringUtil.getApplicationContext());
            } catch (Exception e) {
                throw new UtilException("Exit app error occurred!", e);
            }
            try {
                var sourceArgs = args.getSourceArgs();
                SpringUtil.loadEnvFile(sourceArgs);
                SpringApplication.run(SubtitleTranslationAI.class, sourceArgs);
            } catch (Exception e) {
                throw new UtilException("Failed to restart the application.", e);
            }
        }).start();
    }
}
