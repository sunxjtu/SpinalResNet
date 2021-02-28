import spinal.core._
import spinal.lib._

class Wrom(
  w : Array[Double],
  Qwp : Int,
  Qwr : Int,
  Chout : Int,
  ChoutDivHard : Int
) extends Component {

  var romDepth = 0
  romDepth = w.length / Chout
  romDepth = romDepth * ChoutDivHard

  val io = new Bundle {
    val addr = in UInt(log2Up(romDepth) bits)
    val w    = out Vec(SFix(Qwp exp, -Qwr exp),Chout / ChoutDivHard)
  }

  def Wdata = for(i <- 0 until romDepth) yield {
    val a = i % (romDepth / ChoutDivHard)
    Vec((0 until Chout / ChoutDivHard).map{x => 
      val ret = SFix(Qwp exp, -Qwr exp)
      ret := w(romDepth / ChoutDivHard * (x + i/(romDepth/ChoutDivHard)*(Chout/ChoutDivHard)) + a)
      ret
    })
  }
  val rom = Mem(Vec(SFix(Qwp exp, -Qwr exp),Chout / ChoutDivHard),initialContent = Wdata)
  io.w := rom.readSync(io.addr)
}
