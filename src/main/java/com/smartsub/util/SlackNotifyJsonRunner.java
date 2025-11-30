//package com.smartsub.util;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartsub.dto.NotifyTarget;
//import com.smartsub.service.slack.SlackWebhookService;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.util.List;
//
//@Component
//public class SlackNotifyJsonRunner implements CommandLineRunner {
//
//    private final SlackWebhookService slackService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    public SlackNotifyJsonRunner(SlackWebhookService slackService) {
//        this.slackService = slackService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        File file = new File("C:/Users/USER/PycharmProjects/smartsub-analysis/smartsub-analysis/notify_targets.json");
//
//        if (!file.exists()) {
//            System.out.println("❗ notify_targets.json 파일이 존재하지 않습니다.");
//            return;
//        }
//
//        List<NotifyTarget> targets = objectMapper.readValue(
//            file,
//            new TypeReference<>() {}
//        );
//
//        for (NotifyTarget target : targets) {
//            slackService.sendNotification(target.getMessage());
//        }
//
//        System.out.println("✅ Slack 알림 전송 완료 (" + targets.size() + "건)");
//    }
//}
