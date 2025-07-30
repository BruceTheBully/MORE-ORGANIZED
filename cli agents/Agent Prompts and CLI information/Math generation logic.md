    // --- MATH GENERATION LOGIC ---
        function generateOperationalLogic(shade) {
            const psychoacousticText = shade.psychoacoustic_equivalent;
            let logic = `// OPERATIONAL LOGIC FOR: ${shade.title}\n`;
            logic += `// Morphic Function: ${shade.morphic_function}\n\n`;

            logic += `// Input Signal: S_in(t)\n`;
            logic += `// Output Signal: S_out(t)\n`;
            logic += `// Time: t, Sample Rate: SR\n\n`;

            logic += `// Dynamic Parameters (re-calibrated per state evolution)\n`;
            logic += `param_threshold = ${RAND_FLOAT(-40.0, -5.0).toFixed(2)}; // dB\n`;
            logic += `param_ratio = ${RAND_FLOAT(1.5, 10.0).toFixed(2)};\n`;
            logic += `param_attack = ${RAND_FLOAT(0.1, 200.0).toFixed(2)};  // ms\n`;
            logic += `param_release = ${RAND_FLOAT(50.0, 2000.0).toFixed(2)}; // ms\n`;
            logic += `param_mix = ${RAND_FLOAT(0.0, 1.0).toFixed(2)};     // Wet/Dry mix\n`;
            logic += `param_frequency = ${RAND_FLOAT(20.0, 20000.0).toFixed(0)}; // Hz\n`;
            logic += `param_depth = ${RAND_FLOAT(0.0, 1.0).toFixed(2)};   // General modulation depth\n\n`;

            logic += `// --- Core DSP Operations (Psychoacoustic Interpretation) ---\n`;

            // Simple keyword-based parsing for pseudocode generation from psychoacoustic text
            if (psychoacousticText.includes("RMS to near zero")) {
                logic += `RMS_Current = GetRMS(S_in(t));\nif (RMS_Current < 0.01) {\n    S_out(t) = S_in(t) * ${DB_TO_LINEAR(RAND_FLOAT(-90, -60)).toFixed(3)}; // Hard cut to null\n} else {\n    S_out(t) = S_in(t);\n}\n`;
            }
            if (psychoacousticText.includes("compressed delay tail") && psychoacousticText.includes("feeding back")) {
                logic += `Feedback_In = S_in(t) + (Previous_Delay_Output * param_depth);\nCompressed_Feedback = APPLY_COMPRESSION(Feedback_In, param_threshold, param_ratio, param_attack, param_release);\nDelay_Out_Current = ECHO_ENGINE(Compressed_Feedback, DELAY_TIME_MS: ${RAND_FLOAT(100, 1000).toFixed(0)});\nPrevious_Delay_Output = Delay_Out_Current;\nS_out(t) = MIX_DRY_WET(S_in(t), Delay_Out_Current, param_mix);\n`;
            }
            if (psychoacousticText.includes("Phase velocity slows") || psychoacousticText.includes("phase symmetry")) {
                logic += `Phase_Analyzer = AnalyzePhase(S_in(t));\nPhase_Velocity = GetPhaseVelocity(Phase_Analyzer);\nif (Phase_Velocity < ${RAND_FLOAT(0.01, 0.1).toFixed(3)}) {\n    S_out(t) = SYMMETRY_CORRECT(S_in(t), Align_to_center: TRUE);\n} else {\n    S_out(t) = S_in(t);\n}\n`;
            }
            if (psychoacousticText.includes("reversed audio sample") && psychoacousticText.includes("dissolving memory")) {
                logic += `Reversed_Signal = REVERSE_AUDIO(S_in(t));\nMemory_Buffer_Degradation = APPLY_TAPE_EMULATION(Reversed_Signal, wow_flutter: ${RAND_FLOAT(0.01, 0.1).toFixed(3)}, crackle: ${RAND_FLOAT(0, 0.5).toFixed(2)});\nS_out(t) = DISSOLVE_BUFFER(Memory_Buffer_Degradation, dissolve_rate: ${RAND_FLOAT(0.001, 0.01).toFixed(4)});\n`;
            }
            if (psychoacousticText.includes("Brickwall Glue") && psychoacousticText.includes("signal becomes white")) {
                logic += `S_out(t) = BRICKWALL_GLUE(S_in(t), threshold: ${RAND_FLOAT(-3, -0.5).toFixed(2)}dB);\nS_out(t) = DRIVE_TO_WHITE_NOISE_SATURATION(S_out(t), saturation_amount: ${RAND_FLOAT(0.8, 1.0).toFixed(2)});\n`;
            }
            if (psychoacousticText.includes("pre-delay") && psychoacousticText.includes("GhostAir Bandpass")) {
                logic += `S_delayed = DELAY_LINE(S_in(t), time_ms: ${RAND_FLOAT(10, 50).toFixed(1)});\nS_filtered = BANDPASS_FILTER(S_delayed, center_freq: ${RAND_FLOAT(5000, 15000).toFixed(0)}, Q: ${RAND_FLOAT(5, 20).toFixed(1)});\nS_out(t) = MIX_DRY_WET(S_in(t), S_filtered, ${RAND_FLOAT(0.01, 0.1).toFixed(3)}); // Subtle mix\n`;
            }
            if (psychoacousticText.includes("Kick Drum") && psychoacousticText.includes("Emitter Pulse")) {
                logic += `Transient_Detected = DETECT_KICK_TRANSIENT(S_in(t));\nif (Transient_Detected) {\n    S_out(t) = PULSE_SATURATE(S_in(t), drive: ${RAND_FLOAT(1.0, 3.0).toFixed(1)}, decay_ms: ${RAND_FLOAT(50, 200).toFixed(0)});\n    S_out(t) = REVERB_SEED(S_out(t), time: ${RAND_FLOAT(10, 50).toFixed(0)}, rate: ${RAND_FLOAT(0.1, 1.0).toFixed(1)});\n} else {\n    S_out(t) = S_in(t);\n}\n`;
            }
            if (psychoacousticText.includes("Density Clamp") && psychoacousticText.includes("solid, unmoving wall of sound")) {
                logic += `RMS_Global = GetRMS(S_in(t));\nS_out(t) = APPLY_DENSITY_CLAMP(S_in(t), threshold: ${RAND_FLOAT(-10, -3).toFixed(2)}, ratio: ${RAND_FLOAT(5, 20).toFixed(1)}); // Compresses to a solid wall\nS_out(t) = SNARL_SATURATION(S_out(t), drive: ${RAND_FLOAT(0.5, 1.5).toFixed(1)}, mix: ${RAND_FLOAT(0.5, 1.0).toFixed(1)}); // Adds dense harmonic field\n`;
            }
            if (psychoacousticText.includes("Null Catch") && psychoacousticText.includes("ambient information to slowly swell up")) {
                logic += `Ambient_Detector = DETECT_QUIET_PASSAGES(S_in(t), threshold: ${RAND_FLOAT(-60, -40).toFixed(1)});\nif (Ambient_Detector) {\n    S_out(t) = UPWARD_COMPRESSION(S_in(t), threshold: ${RAND_FLOAT(-50, -30).toFixed(1)}, ratio: ${RAND_FLOAT(1.2, 1.8).toFixed(1)}); // Lifts ambient\n    S_out(t) = SLOW_REVERB_SWELL(S_out(t), decay: ${RAND_FLOAT(5000, 15000).toFixed(0)}, shimmer: ${RAND_FLOAT(0, 1).toFixed(2)});\n} else {\n    S_out(t) = S_in(t);\n}\n`;
            }
            if (psychoacousticText.includes("glitch roll") && psychoacousticText.includes("raw, undifferentiated signal")) {
                logic += `Transient_Randomizer = GLITCH_ROLL_GEN(S_in(t), rate: ${RAND_FLOAT(5, 20).toFixed(1)}Hz, chaos: ${RAND_FLOAT(0.5, 1.0).toFixed(