import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun TimePickerField() {
    val context = LocalContext.current
    val timeFormatter = remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }

    var selectedTime by remember { mutableStateOf("") }

    fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val selectedCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, selectedHour)
                    set(Calendar.MINUTE, selectedMinute)
                }
                selectedTime = timeFormatter.format(selectedCalendar.time)
            },
            hour,
            minute,
            false
        ).show()
    }

    // Campo de entrada con icono de reloj
    OutlinedTextField(
        value = selectedTime,
        onValueChange = { },
        readOnly = true,
        label = { Text("Select Time") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showTimePickerDialog() },
        trailingIcon = {
            IconButton(onClick = { showTimePickerDialog() }) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Seleccionar Hora"
                )
            }
        }
    )
}
