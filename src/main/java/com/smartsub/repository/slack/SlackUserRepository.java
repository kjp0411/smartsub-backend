package com.smartsub.repository.slack;

import com.smartsub.domain.slack.SlackUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface SlackUserRepository extends JpaRepository<SlackUser, Long> {
    Optional<SlackUser> findByMemberId(Long memberId);

    Optional<SlackUser> findBySlackUserId(String slackUserId);
}
