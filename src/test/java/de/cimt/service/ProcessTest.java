package de.cimt.service;

import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.junit.jupiter.api.Test;
import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.model.xml.test.assertions.ModelAssertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({ProcessEngineExtension.class, OutputCaptureExtension.class})
@SpringBootTest
@Sql("/data.sql")
class ProcessTest {

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    @Deployment(resources = {"leave_manager.bpmn"})
    public void TestAcceptedLeave(CapturedOutput output) {
        Map<String, Object> variables = new HashMap<String, Object>();
       ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_1", variables);
        List<Task> taskList = taskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .list();
        assertThat(taskList).isNotNull();
        assertThat(taskList).hasSize(1);
        Task applyForLeave = taskList.get(0);
        variables.put("email", "tomas@gmail.com");
        taskService().complete(applyForLeave.getId(),variables);
        taskList = taskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .list();
        assertThat(taskList).isNotNull();
        assertThat(taskList).hasSize(1);
        Task reviewLeaveApplicationTask = taskList.get(0);
        Object leaveBalance =  runtimeService().getVariable(processInstance.getId(), "leave_balance");
        assertEquals((int)leaveBalance,20);
        variables.put("accept_leave", Boolean.TRUE);
        taskService().complete(reviewLeaveApplicationTask.getId(),variables);
        assertThat(output).contains("Accepted leave sent successfully");

    }
    @Test
    @Deployment(resources = {"leave_manager.bpmn"})
    public void TestRejectedLeave(CapturedOutput output) {
        Map<String, Object> variables = new HashMap<String, Object>();
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("Process_1", variables);
        List<Task> taskList = taskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .list();
        assertThat(taskList).isNotNull();
        assertThat(taskList).hasSize(1);
        Task applyForLeave = taskList.get(0);
        variables.put("email", "tomas@gmail.com");
        taskService().complete(applyForLeave.getId(),variables);
        taskList = taskService()
                .createTaskQuery()
                .processInstanceId(processInstance.getId())
                .list();
        assertThat(taskList).isNotNull();
        assertThat(taskList).hasSize(1);
        Task reviewLeaveApplicationTask = taskList.get(0);
        Object leaveBalance =  runtimeService().getVariable(processInstance.getId(), "leave_balance");
        assertEquals((int)leaveBalance,20);
        variables.put("accept_leave", Boolean.FALSE);
        taskService().complete(reviewLeaveApplicationTask.getId(),variables);
        assertThat(output).contains("Rejected leave sent successfully");

    }


}