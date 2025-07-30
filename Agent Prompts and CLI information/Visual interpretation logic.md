  // --- VISUAL INTERPRETATIONS FOR EACH SHADE (ON CANVAS) ---
        // This maps 'visual_type' from SHADES_OF_COLLAPSE to drawing functions.
        const canvas = document.getElementById('healingCanvas');
        const ctx = canvas.getContext('2d');
        const W = canvas.width;
        const H = canvas.height;
        let animationFrameId;
        let animationTime = 0;
        let currentShadeVisual = null;

        function getGray(value) { return `rgb(${Math.floor(value * 255)}, ${Math.floor(value * 255)}, ${Math.floor(value * 255)})`; }

        const visualFunctions = {
            FadeOutToVoid: (ctx, t) => {
                const alpha = Math.max(0, 1 - (t * 0.005 % 1));
                ctx.fillStyle = `rgba(0,0,0,${alpha})`;
                ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.8 * (1-alpha)); // Fading light
                ctx.beginPath(); ctx.arc(W/2,H/2, 50 * alpha, 0, Math.PI*2); ctx.fill();
            },
            SpiralEcho: (ctx, t) => {
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 1;
                for (let i = 0; i < 5; i++) {
                    const angle = (t * 0.05 + i * Math.PI/2) % (Math.PI*2);
                    const radius = (W/4) * ( (Math.sin(angle) + 1) / 2 );
                    ctx.globalAlpha = 0.8 * (1 - (radius / (W/4)));
                    ctx.beginPath(); ctx.arc(W/2 + Math.cos(t*0.03)*50, H/2 + Math.sin(t*0.04)*50, radius + 20, 0, Math.PI*2); ctx.stroke();
                }
                ctx.globalAlpha = 1;
            },
            SymmetryBreathing: (ctx, t) => {
                const breathe = (Math.sin(t * 0.02) + 1) / 2;
                ctx.fillStyle = getGray(0.1 + breathe * 0.2);
                ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7 + breathe * 0.3);
                ctx.lineWidth = 2;
                for(let i = 0; i < H; i += 20) {
                    ctx.beginPath();
                    ctx.moveTo(0, i); ctx.lineTo(W, i);
                    ctx.stroke();
                    ctx.beginPath();
                    ctx.moveTo(W/2, H/2); ctx.lineTo(i/H * W, i); ctx.stroke();
                }
            },
            ReverseWaveDissolve: (ctx, t) => {
                ctx.fillStyle = getGray(0.9); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.3); ctx.lineWidth = 2;
                ctx.beginPath();
                for (let x = 0; x < W; x += 5) {
                    const y = H/2 + Math.sin((x/W*4*Math.PI) + t*0.1) * (H/4) * (1 - (t*0.005 % 1));
                    if(x === 0) ctx.moveTo(x, y); else ctx.lineTo(x, y);
                }
                ctx.stroke();
            },
            WhiteOutCollapse: (ctx, t) => {
                const intensity = (t * 0.01 % 1);
                ctx.fillStyle = getGray(intensity);
                ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(1 - intensity);
                ctx.beginPath(); ctx.arc(W/2,H/2, 100 * intensity, 0, Math.PI*2); ctx.fill();
            },
            FutureBleedGlimmer: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 1;
                for (let i = 0; i < 10; i++) {
                    const x_offset = (t * 0.8 + i * 20) % W;
                    ctx.globalAlpha = 0.2 + Math.sin(t * 0.07 + i) * 0.1;
                    ctx.beginPath();
                    ctx.moveTo(x_offset, H * 0.2);
                    ctx.lineTo(x_offset + 50, H * 0.8);
                    ctx.stroke();
                }
                ctx.globalAlpha = 1;
            },
            RadialPulse: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                const pulse_intensity = (Math.sin(t * 0.1) + 1) / 2;
                ctx.fillStyle = getGray(0.5 + pulse_intensity * 0.5);
                ctx.beginPath(); ctx.arc(W/2, H/2, 20 + pulse_intensity * 100, 0, Math.PI * 2); ctx.fill();
            },
            SingularityCompression: (ctx, t) => {
                ctx.fillStyle = getGray(0.0); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.9 * ((Math.sin(t * 0.05) + 1) / 2));
                const size = 200 - (t * 2 % 200);
                ctx.fillRect(W/2 - size/2, H/2 - size/2, size, size);
            },
            VoidExpansion: (ctx, t) => {
                ctx.fillStyle = getGray(0.9); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.2); ctx.lineWidth = 2;
                const radius = (t * 0.5) % (Math.min(W,H)/2);
                ctx.beginPath(); ctx.arc(W/2, H/2, radius, 0, Math.PI * 2); ctx.stroke();
            },
            ChaoticSpark: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.8);
                for(let i=0; i<20; i++) {
                    const x = Math.random() * W;
                    const y = Math.random() * H;
                    const size = Math.random() * 10 + 5;
                    ctx.fillRect(x,y,size,size);
                }
            },
            TracingPath: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 3;
                ctx.beginPath();
                ctx.moveTo(0, H/2);
                for(let x = 0; x < W; x += 10) {
                    const y = H/2 + Math.sin(x*0.05 + t*0.05) * 50;
                    ctx.lineTo(x,y);
                }
                ctx.stroke();
            },
            SpaceTimeWarp: (ctx, t) => {
                ctx.lineWidth = 1;
                for (let i = 0; i < 15; i++) {
                    ctx.strokeStyle = getGray(0.2 + (i / 15) * 0.5);
                    ctx.beginPath();
                    ctx.moveTo(W / 15 * i + Math.sin(t * 0.03 + i) * 15, 0);
                    ctx.lineTo(W / 15 * i + Math.sin(t * 0.03 + i) * 15, H);
                    ctx.moveTo(0, H / 15 * i + Math.cos(t * 0.04 + i) * 15);
                    ctx.lineTo(W, H / 15 * i + Math.cos(t * 0.04 + i) * 15);
                    ctx.stroke();
                }
            },
            CohesiveAlignment: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 2;
                ctx.beginPath();
                for(let i=0; i<10; i++) {
                    const y = H/10 * i;
                    ctx.moveTo(0, y);
                    ctx.lineTo(W, y);
                    ctx.stroke();
                }
                ctx.fillStyle = getGray(0.5);
                ctx.beginPath(); ctx.arc(W/2, H/2, 50 + Math.sin(t*0.05)*20, 0, Math.PI*2); ctx.fill();
            },
            SelfAwareBloom: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 2;
                const bloomRadius = 50 + (Math.sin(t * 0.03) + 1) / 2 * 100;
                ctx.beginPath(); ctx.arc(W/2, H/2, bloomRadius, 0, Math.PI*2); ctx.stroke();
                ctx.fillStyle = getGray(0.9);
                ctx.beginPath(); ctx.arc(W/2, H/2, 10 + (Math.sin(t*0.1)+1)/2 * 20, 0, Math.PI*2); ctx.fill(); // Inner pulse
            },
            InterlockingLattice: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.6); ctx.lineWidth = 1;
                const gridSize = 50;
                for (let x = 0; x <= W; x += gridSize) {
                    for (let y = 0; y <= H; y += gridSize) {
                        ctx.beginPath();
                        ctx.moveTo(x + Math.sin(t*0.05 + x)*5, y + Math.cos(t*0.06 + y)*5);
                        ctx.lineTo(x + gridSize + Math.sin(t*0.05 + x + gridSize)*5, y + Math.cos(t*0.06 + y)*5);
                        ctx.lineTo(x + gridSize + Math.sin(t*0.05 + x + gridSize)*5, y + gridSize + Math.cos(t*0.06 + y + gridSize)*5);
                        ctx.lineTo(x + Math.sin(t*0.05 + x)*5, y + gridSize + Math.cos(t*0.06 + y + gridSize)*5);
                        ctx.closePath();
                        ctx.stroke();
                    }
                }
            },
            UnbreakableBond: (ctx, t) => {
                ctx.fillStyle = getGray(0.08); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 3;
                const bondWidth = 50 + Math.sin(t*0.03)*20;
                ctx.beginPath();
                ctx.moveTo(0, H/2 - bondWidth/2); ctx.lineTo(W, H/2 - bondWidth/2);
                ctx.moveTo(0, H/2 + bondWidth/2); ctx.lineTo(W, H/2 + bondWidth/2);
                ctx.stroke();
                ctx.fillStyle = getGray(0.5 + Math.sin(t*0.05)*0.2);
                ctx.beginPath(); ctx.arc(W/2, H/2, 20 + Math.cos(t*0.07)*10, 0, Math.PI*2); ctx.fill();
            },
            PerfectSphere: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.8);
                ctx.beginPath(); ctx.arc(W/2, H/2, Math.min(W,H)/2 - 20, 0, Math.PI*2); ctx.fill();
                ctx.strokeStyle = getGray(0.2); ctx.lineWidth = 1;
                for(let i = 0; i < 360; i += 30) {
                    ctx.beginPath(); ctx.arc(W/2, H/2, Math.min(W,H)/2 - 20, i*Math.PI/180, (i+15)*Math.PI/180); ctx.stroke();
                }
            },
            GentleWarp: (ctx, t) => {
                ctx.fillStyle = getGray(0.8); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.3); ctx.lineWidth = 1;
                for(let i=0; i<H; i+=10) {
                    ctx.beginPath();
                    ctx.moveTo(0, i + Math.sin(t*0.05 + i*0.01) * 5);
                    ctx.lineTo(W, i + Math.sin(t*0.05 + i*0.01 + Math.PI)*5);
                    ctx.stroke();
                }
            },
            PhaseInversionShear: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.9); ctx.lineWidth = 3;
                const shearOffset = Math.sin(t * 0.05) * 50;
                ctx.beginPath();
                ctx.moveTo(0, H/2); ctx.lineTo(W, H/2); ctx.stroke(); // Original signal line
                ctx.strokeStyle = getGray(0.3); ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.moveTo(0, H/2 - shearOffset); ctx.lineTo(W, H/2 + shearOffset); // Sheared line
                ctx.stroke();
            },
            VoiceManifestation: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 2;
                ctx.beginPath();
                for (let i = 0; i < W; i += 5) {
                    const y = H/2 + Math.sin(i * 0.05 + t * 0.05) * (H/4) * (0.5 + Math.sin(t*0.03)*0.5);
                    if(i === 0) ctx.moveTo(i,y); else ctx.lineTo(i,y);
                }
                ctx.stroke();
                ctx.fillStyle = getGray(0.6 + Math.sin(t*0.08)*0.3);
                ctx.font = '30px Arial';
                ctx.fillText("VOICE", W/2 - 40, H/2 - 50); // Simple text as a conceptual voice
            },
            RhythmicGridLock: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 1;
                const gridSize = 40;
                for(let x=0; x<=W; x+=gridSize) {
                    for(let y=0; y<=H; y+=gridSize) {
                        ctx.strokeRect(x,y,gridSize,gridSize);
                    }
                }
                ctx.fillStyle = getGray(0.9);
                const pulseX = ((t*0.1) % W);
                const pulseY = ((t*0.08) % H);
                ctx.fillRect(pulseX, pulseY, 20,20);
            },
            SpectralDance: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 1;
                for (let i = 0; i < 50; i++) {
                    const x1 = Math.sin(t*0.03 + i) * W/2 + W/2;
                    const y1 = Math.cos(t*0.04 + i) * H/2 + H/2;
                    const x2 = Math.sin(t*0.05 + i) * W/2 + W/2;
                    const y2 = Math.cos(t*0.06 + i) * H/2 + H/2;
                    ctx.beginPath(); ctx.moveTo(x1,y1); ctx.lineTo(x2,y2); ctx.stroke();
                }
            },
            SeamlessStream: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 3;
                ctx.lineCap = 'round';
                for(let i=0; i<3; i++) {
                    ctx.beginPath();
                    ctx.moveTo(0, H/4 * (i+1));
                    for(let x=0; x<W; x+=10) {
                        const y = H/4 * (i+1) + Math.sin(x*0.03 + t*0.05 + i) * 15;
                        ctx.lineTo(x,y);
                    }
                    ctx.stroke();
                }
            },
            PerfectEquilibrium: (ctx, t) => {
                ctx.fillStyle = getGray(0.95); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.1); ctx.lineWidth = 5;
                ctx.beginPath();
                ctx.arc(W/2, H/2, Math.min(W,H)/3, 0, Math.PI*2); ctx.stroke();
                ctx.beginPath();
                ctx.moveTo(W/2, 0); ctx.lineTo(W/2, H);
                ctx.moveTo(0, H/2); ctx.lineTo(W, H/2);
                ctx.stroke();
            },
            SilentGenesis: (ctx, t) => {
                ctx.fillStyle = getGray(0.9); ctx.fillRect(0,0,W,H);
                const sparkAlpha = (t * 0.005 % 1);
                ctx.fillStyle = `rgba(100,100,100, ${sparkAlpha})`;
                ctx.beginPath(); ctx.arc(W/2, H/2, sparkAlpha * 100, 0, Math.PI*2); ctx.fill();
            },
            SolidFoundation: (ctx, t) => {
                ctx.fillStyle = getGray(0.0); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.2 + Math.sin(t*0.02)*0.1);
                ctx.fillRect(0, H*0.8, W, H*0.2); // Solid dark base
                ctx.strokeStyle = getGray(0.5); ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.moveTo(0, H*0.8); ctx.lineTo(W, H*0.8);
                ctx.stroke();
            },
            SproutingGrowth: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 1;
                for (let i = 0; i < 20; i++) {
                    const length = (t * 0.5 + i * 10) % 200;
                    ctx.beginPath();
                    ctx.moveTo(W/2, H);
                    ctx.lineTo(W/2 + Math.cos(i) * length, H - length);
                    ctx.stroke();
                }
            },
            FocusedBurst: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                const burstIntensity = (Math.sin(t * 0.1) + 1) / 2;
                ctx.fillStyle = getGray(0.9 * burstIntensity);
                ctx.beginPath();
                ctx.arc(W/2, H/2, 30 * burstIntensity, 0, Math.PI*2);
                ctx.fill();
            },
            AbstractToExecutable: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 1;
                for (let i = 0; i < 20; i++) {
                    ctx.beginPath();
                    ctx.moveTo(W * Math.random(), H * Math.random());
                    ctx.lineTo(W * Math.random(), H * Math.random());
                    ctx.stroke();
                }
                ctx.fillStyle = getGray(0.9);
                ctx.font = '20px monospace';
                ctx.fillText("CODE", W/2 - 30, H/2);
            },
            ResonantQuestion: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.6); ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.arc(W/2, H/2, 100 + Math.sin(t * 0.05) * 50, 0, Math.PI * 2);
                ctx.stroke();
                ctx.beginPath();
                ctx.moveTo(W/2, H/2); ctx.lineTo(W/2 + Math.cos(t*0.1)*50, H/2 + Math.sin(t*0.1)*50); ctx.stroke();
            },
            BreakthroughAlignment: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.9); ctx.lineWidth = 3;
                const lineProgress = (t * 0.005) % 1;
                ctx.beginPath();
                ctx.moveTo(0, H * lineProgress);
                ctx.lineTo(W, H * (1 - lineProgress));
                ctx.stroke();
                ctx.fillStyle = getGray(0.7);
                ctx.beginPath(); ctx.arc(W/2, H/2, 30 + Math.sin(t*0.05)*15, 0, Math.PI*2); ctx.fill();
            },
            InterlockingSynergy: (ctx, t) => {
                ctx.fillStyle = getGray(0.08); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 2;
                const count = 5;
                for (let i = 0; i < count; i++) {
                    const xOffset = W / count * i;
                    const yOffset = H / count * i;
                    ctx.beginPath();
                    ctx.moveTo(xOffset, 0); ctx.lineTo(W - xOffset, H); ctx.stroke();
                    ctx.beginPath();
                    ctx.moveTo(0, yOffset); ctx.lineTo(W, H - yOffset); ctx.stroke();
                }
            },
            LockedTrajectory: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 4;
                ctx.beginPath();
                ctx.moveTo(0, H/2);
                ctx.lineTo(W, H/2);
                ctx.stroke();
                ctx.fillStyle = getGray(0.9);
                ctx.beginPath(); ctx.arc((t*2)%W, H/2, 10, 0, Math.PI*2); ctx.fill(); // Moving dot
            },
            PerfectMapping: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 1;
                const density = 20;
                for(let x = 0; x <= W; x += density) {
                    for(let y = 0; y <= H; y += density) {
                        ctx.beginPath();
                        ctx.arc(x,y,5 + Math.sin(t*0.05 + x + y)*3, 0, Math.PI*2);
                        ctx.stroke();
                    }
                }
            },
            DeepScanRevelation: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 0.5;
                for(let i=0; i<H; i+=5) {
                    ctx.beginPath();
                    ctx.moveTo(0, i);
                    ctx.lineTo(W, i + Math.sin(t*0.03 + i)*10);
                    ctx.stroke();
                }
            },
            InsightEmergence: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.8);
                const radius = (t * 0.5 % 100) + 20;
                ctx.beginPath(); ctx.arc(W/2,H/2,radius,0,Math.PI*2); ctx.fill();
            },
            CreativeManifestation: (ctx, t) => {
                ctx.fillStyle = getGray(0.08); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.9);
                ctx.font = '25px monospace';
                ctx.fillText("MANIFEST", (t*2 % W), H/2);
            },
            DynamicResponse: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 2;
                ctx.beginPath();
                for(let i=0; i<W; i+=10) {
                    const h = (Math.sin(i*0.05 + t*0.08)+1)/2 * H * 0.4;
                    ctx.moveTo(i, H/2 - h); ctx.lineTo(i, H/2 + h);
                }
                ctx.stroke();
            },
            ArchivalSecurity: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.5); ctx.lineWidth = 1;
                for(let i=0; i<10; i++) {
                    ctx.strokeRect(W/2 - 100 + Math.sin(t*0.03+i)*5, H/2 - 50 + Math.cos(t*0.04+i)*5, 200,100);
                }
            },
            BlueprintRecall: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 0.5;
                for(let i=0; i<20; i++) {
                    ctx.beginPath();
                    ctx.moveTo(0, (i*20 + t*0.5) % H);
                    ctx.lineTo(W, (i*20 + t*0.5 + H/2) % H);
                    ctx.stroke();
                }
            },
            EvolvingAdaptation: (ctx, t) => {
                ctx.fillStyle = getGray(0.08); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.7); ctx.lineWidth = 1;
                for(let i=0; i<5; i++) {
                    const scale = 0.5 + (Math.sin(t*0.05 + i)+1)/2 * 0.3;
                    ctx.beginPath();
                    ctx.arc(W/2, H/2, 50 * scale + i*20, 0, Math.PI*2);
                    ctx.stroke();
                }
            },
            ExponentialGrowth: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.9); ctx.lineWidth = 2;
                const growFactor = (t * 0.005 % 1);
                ctx.beginPath();
                ctx.arc(W/2, H/2, growFactor * Math.min(W,H)/2, 0, Math.PI*2);
                ctx.stroke();
            },
            SubtleCalibration: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.5); ctx.lineWidth = 1;
                for(let i=0; i<H; i+=10) {
                    ctx.beginPath();
                    ctx.moveTo(0, i);
                    ctx.lineTo(W, i + Math.sin(t*0.02 + i*0.01) * 3);
                    ctx.stroke();
                }
            },
            PureExpression: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.9); ctx.lineWidth = 3;
                ctx.beginPath();
                for (let x = 0; x < W; x += 10) {
                    const y = H/2 + Math.sin(x * 0.1 + t * 0.05) * (H/4);
                    if(x===0) ctx.moveTo(x,y); else ctx.lineTo(x,y);
                }
                ctx.stroke();
            },
            DreamCanvas: (ctx, t) => {
                ctx.fillStyle = getGray(0.15); ctx.fillRect(0,0,W,H);
                ctx.fillStyle = getGray(0.6);
                for(let i=0; i<100; i++) {
                    const x = (Math.random() * W + t*0.2) % W;
                    const y = (Math.random() * H + t*0.1) % H;
                    ctx.beginPath(); ctx.arc(x,y,5,0,Math.PI*2); ctx.fill();
                }
            },
            MythFormation: (ctx, t) => {
                ctx.fillStyle = getGray(0.05); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.4); ctx.lineWidth = 1;
                for(let i=0; i<20; i++) {
                    ctx.beginPath();
                    ctx.moveTo(W/2, H/2);
                    ctx.lineTo(Math.sin(t*0.03 + i)*W/2 + W/2, Math.cos(t*0.04 + i)*H/2 + H/2);
                    ctx.stroke();
                }
            },
            SoulResolution: (ctx, t) => {
                ctx.fillStyle = getGray(0.9); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.2); ctx.lineWidth = 2;
                ctx.beginPath();
                ctx.arc(W/2,H/2, Math.min(W,H)/3, 0, Math.PI*2);
                ctx.stroke();
                ctx.fillStyle = getGray(0.5);
                ctx.beginPath(); ctx.arc(W/2, H/2, 10 + Math.sin(t*0.05)*5, 0, Math.PI*2); ctx.fill();
            },
            BoundlessAwareness: (ctx, t) => {
                ctx.fillStyle = getGray(0.1); ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.8); ctx.lineWidth = 0.5;
                for(let i=0; i<W; i+=10) {
                    ctx.beginPath();
                    ctx.moveTo(i, 0); ctx.lineTo(i + Math.sin(t*0.05 + i)*20, H);
                    ctx.stroke();
                }
            },
            UnifiedExistence: (ctx, t) => {
                ctx.fillStyle = getGray(0.5 + Math.sin(t*0.02)*0.2); // Gentle pulse
                ctx.fillRect(0,0,W,H);
                ctx.strokeStyle = getGray(0.1); ctx.lineWidth = 1;
                ctx.beginPath(); ctx.arc(W/2, H/2, Math.min(W,H)/2 * (0.8 + Math.sin(t*0.03)*0.1), 0, Math.PI*2); ctx.stroke();
            },
            ObserverMergeNexus: (ctx, t) => {
                ctx.fillStyle = getGray(0.0); ctx.fillRect(0,0,W,H);
                const spark = (t * 0.01) % 1;
                ctx.fillStyle = `rgba(255,255,255,${1-spark})`; // White spark fading
                ctx.beginPath(); ctx.arc(W/2, H/2, spark * 100, 0, Math.PI*2); ctx.fill();
            },
            // Fallback for any shade not specifically coded
            defaultVisual: (ctx, t) => {
                ctx.fillStyle = getGray(0.1);
                ctx.fillRect(0, 0, W, H);
                ctx.strokeStyle = getGray(0.7);
                ctx.lineWidth = 2;
                ctx.beginPath();
                for (let x = 0; x < W; x += 10) {
                    const y_amplitude = (Math.sin(x * 0.05 + t * 0.03) + 1) / 2 * H * 0.3;
                    ctx.moveTo(x, H / 2 - y_amplitude);
                    ctx.lineTo(x, H / 2 + y_amplitude);
                }
                ctx.stroke();
            }
        };