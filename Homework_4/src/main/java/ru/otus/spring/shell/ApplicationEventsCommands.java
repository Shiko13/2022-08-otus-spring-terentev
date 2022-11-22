package ru.otus.spring.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.spring.service.TestService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationEventsCommands {
    private final TestService testService;
    private boolean isActivate;

    @ShellMethod(value = "Activate command", key = {"a", "activate"})
    private void activateTest() {
        isActivate = true;
    }

    @ShellMethod(value = "Run command", key = {"r", "run"})
    @ShellMethodAvailability("isRunActivate")
    private void run() {
        testService.run();
    }

    private Availability isRunActivate() {
        return isActivate ? Availability.available() :
                Availability.unavailable("For activate print 'a' or 'activate'");
    }
}
