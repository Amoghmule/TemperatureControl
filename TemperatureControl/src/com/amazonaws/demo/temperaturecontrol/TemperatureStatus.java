

package com.amazonaws.demo.temperaturecontrol;

/**
 * <pre>
 * {
 *   "state": {
 *     "desired": {
 *       "intTemp": 72,
 *       "extTemp": 45,
 *       "curState": "stopped"
 *     },
 *     "delta": {
 *       "intTemp": 72,
 *       "extTemp": 45,
 *       "curState": "stopped"
 *     }
 *   },
 *   "metadata": {
 *     "desired": {
 *       "intTemp": {
 *         "timestamp": 1449791237
 *       },
 *       "extTemp": {
 *         "timestamp": 1449791237
 *       },
 *       "curState": {
 *         "timestamp": 1449791237
 *       }
 *     }
 *   },
 *   "version": 6151,
 *   "timestamp": 1449791576
 * }
 * </pre>
 */
public class TemperatureStatus {
    public State state;

    TemperatureStatus() {
        state = new State();
    }

    public class State {
        Desired desired;
        Delta delta;
        Reported reported;

        State() {
            desired = new Desired();
            delta = new Delta();
            reported = new Reported();
        }

        public class Desired {
            Desired() {
            }


            public String heart_rate;
           public String position;
            public String led1;

//            public Integer intTemp;
//            public Integer extTemp;
//            public String curState;
        }

        public class Reported {
            Reported() {
            }


            public String heart_rate;
            public String position;
            public String led1;

//            public Integer intTemp;
//            public Integer extTemp;
//            public String curState;
        }

        public class Delta {
            Delta() {
            }

            public String heart_rate;
            public String position;
            public String led1;
          // public String buzzer;
//            public Integer intTemp;
//            public Integer extTemp;
//            public String curState;
        }
    }

    public Long version;
    public Long timestamp;
}
