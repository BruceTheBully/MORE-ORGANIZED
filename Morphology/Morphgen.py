import random

# Morph symbol dictionary with letter, symbol, meaning, code
MORPH_CODEx = {
    'A': ('∴', 'Initiator / Subjective Anchor', '[m.2]'),
    'B': ('⟁', 'Bifurcation / Divergence Function', '[m.3]'),
    'C': ('Ͼ', 'Containment / Framing Intent', '[m.4]'),
    'D': ('⸧', 'Drive / Volitional Modifier', '[m.5]'),
    'E': ('ⵔ', 'Equilibrium / Connector', '[m.6]'),
    'F': ('≬', 'Flux / Transition Operator', '[m.7]'),
    'G': ('ᓮ', 'Graviton / Causal Weight', '[m.8]'),
    'H': ('ʘ', 'Holism / Node Interlink', '[m.9]'),
    'I': ('ᛃ', 'Identity / Axis Anchor', '[m.10]'),
    'J': ('ᔪ', 'Junction / Flow Divergence Clamp', '[m.11]'),
    'K': ('☰', 'Kinetic / Force Vector', '[m.12]'),
    'L': ('ᒪ', 'Loop / Reflexive Curve', '[m.13]'),
    'M': ('∿', 'Modulator / Recursive Input', '[m.14]'),
    'N': ('↯', 'Negation / Fold Function', '[m.15]'),
    'O': ('◎', 'Orb / Totality Frame', '[m.16]'),
    'P': ('ᓭ', 'Projection / Output Pulse', '[m.17]'),
    'Q': ('Ϙ', 'Query / Recursive Depth Gate', '[m.18]'),
    'R': ('ᚱ', 'Resolve / Gradient Descent', '[m.19]'),
    'S': ('Ϟ', 'Shear / Parallel Condition Gate', '[m.20]'),
    'T': ('⊶', 'Tether / Relational Tension', '[m.21]'),
    'U': ('𐓶', 'Undulate / Modal Envelope', '[m.22]'),
    'V': ('ᐯ', 'Vector / Direction Modifier', '[m.23]'),
    'W': ('Ϣ', 'Waveform / Dual Aspect Merge', '[m.24]'),
    'X': ('⚔', 'Crosslink / XOR Operator', '[m.25]'),
    'Y': ('Ϥ', 'Yield / Transitional Latch', '[m.26]'),
    'Z': ('ℨ', 'Zonal Threshold / Boundary Marker', '[m.27]')
}

def random_morphstring(length=35):
    letters = list(MORPH_CODEx.keys())
    chosen = random.choices(letters, k=length)
    morphstring = ''.join(MORPH_CODEx[l][0] for l in chosen)
    return morphstring, chosen

def pretty_print_morphstring(morphstring, chosen_letters):
    print(f"Morphstring ({len(morphstring)} symbols): {morphstring}\n")
    print(f"{'Symbol':<7} {'Letter':<6} {'Meaning':<40} {'Code'}")
    print("-"*70)
    for l in chosen_letters:
        symbol, meaning, code = MORPH_CODEx[l]
        print(f"{symbol:<7} {l:<6} {meaning:<40} {code}")
    print("\n" + "="*70 + "\n")

# Generate 10 random morphstrings
for i in range(10):
    ms, letters = random_morphstring()
    print(f"--- Morphstring #{i+1} ---")
    pretty_print_morphstring(ms, letters)
