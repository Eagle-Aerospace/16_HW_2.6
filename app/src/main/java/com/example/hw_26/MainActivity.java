package com.example.hw_26;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView output;

    // дополнительное поля логики
    private long startTime = 0L; // стартовое время
    private long timeInMilliseconds = 0L; // текущее время в миллисекундах
    private long timePause = 0L; // время в состоянии "Пауза"
    private long updatedTime = 0L; // обновлённое время

    private Handler handler = new Handler(); // обработчик очереди сообщений

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        output = findViewById(R.id.timer);
    }

    public void onStart(View view) {
        startTime = SystemClock.uptimeMillis(); // Миллисекунды с момента загрузки (не считая времени, проведенного в глубоком сне)
        handler.removeCallbacks(backTimerThread); // удаление из очереди данного потока
        handler.postDelayed(updateTimerThread, 0); // запуск потока с нулевой задержкой
    }

    public void onPause(View view) {
        timePause += timeInMilliseconds; // фиксирование времени в момент нажатия кнопки
        handler.removeCallbacks(updateTimerThread); // удаление из очереди данного потока
        handler.removeCallbacks(backTimerThread); // удаление из очереди данного потока

    }

    public void onStop(View view) {
        startTime = SystemClock.uptimeMillis();//0L;
        handler.removeCallbacks(updateTimerThread); // удаление из очереди данного потока
        handler.removeCallbacks(backTimerThread); // удаление из очереди данного потока
        output.setText("00:00:00:000"); // загрузка сообщения о прекращении отсчёта времени
    }

    // создание нового потока для обновления времени с помощью объекта интерфейса Runnable
    private Runnable updateTimerThread = new Runnable() {
        public void run() { // внутри метода run() помещается код выполняемого потока
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime; // определение текущего времени
            updatedTime = timePause + timeInMilliseconds; // обновлённое время

            int milliseconds = (int) (updatedTime % 1000); // определение количества миллисекунд
            int second = (int) (updatedTime / 1000); // определение количества секунд
            int minute = second / 60; // определение количества минут
            int hour = minute / 60; // определение количества часов
            int day = hour / 24; // определение количества дней

            second = second % 60; // ограничение количества секунд 60 секундами
            minute = minute % 60; // ограничение количества минут 60 минутами
            hour = hour % 24; // ограничение количества часов 24 часами

            // запись времени в окне вывода информации
            output.setText("" + day + ":" + hour + ":" + minute + ":" + String.format("%02d", second) + ":" + String.format("%03d", milliseconds));
            handler.postDelayed(this, 0); // запуск потока с нулевой задержкой
        }
    };

    private Runnable backTimerThread = new Runnable() {
        public void run() { // внутри метода run() помещается код выполняемого потока

            timeInMilliseconds = SystemClock.uptimeMillis() - startTime; // определение текущего времени
            updatedTime = timePause - timeInMilliseconds; // обновлённое время
            if (updatedTime >= 0) {

                int milliseconds = (int) (updatedTime % 1000); // определение количества миллисекунд
                int second = (int) (updatedTime / 1000); // определение количества секунд
                int minute = second / 60; // определение количества минут
                int hour = minute / 60; // определение количества часов
                int day = hour / 24; // определение количества дней

                second = second % 60; // ограничение количества секунд 60 секундами
                minute = minute % 60; // ограничение количества минут 60 минутами
                hour = hour % 24; // ограничение количества часов 24 часами

                // запись времени в окне вывода информации
                output.setText("" + day + ":" + hour + ":" + minute + ":" + String.format("%02d", second) + ":" + String.format("%03d", milliseconds));
                handler.postDelayed(this, 0); // запуск потока с нулевой задержкой
            } else {
                startTime = 0L;
                handler.removeCallbacks(backTimerThread); // удаление из очереди данного потока
                output.setText("00:00:00:000"); // загрузка сообщения о прекращении отсчёта времени
            }
        }
    };

    public void onBackTime(View view) {
        startTime = SystemClock.uptimeMillis(); // Миллисекунды с момента загрузки (не считая времени, проведенного в глубоком сне)
//        timePause += timeInMilliseconds; // фиксирование времени в момент нажатия кнопки
//        handler.removeCallbacks(updateTimerThread); // удаление из очереди данного потока
        handler.postDelayed(backTimerThread, 0); // запуск потока с нулевой задержкой

    }
}