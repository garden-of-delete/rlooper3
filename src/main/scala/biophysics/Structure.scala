package biophysics

import bioinformatics.EnergizedLoci

case class Structure(
    structureType: String,
    location: EnergizedLoci,
    freeEnergy: Double,
    boltzmannFactor: Double
)
