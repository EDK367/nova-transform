package org.nova.web.dto.request.gymclass;

import java.time.LocalTime;

public record UpdateAttendanceExitRequest(
        LocalTime exitTime
) {
}
