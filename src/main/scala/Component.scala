/**
  * Created by adrij_000 on 24/04/2017.
  */
sealed trait Component {

}

case class SpriteRenderer(texture: Texture) extends Component
