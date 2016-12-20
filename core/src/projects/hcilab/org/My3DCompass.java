package projects.hcilab.org;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;


import java.util.HashMap;

import sun.rmi.runtime.Log;

import static com.badlogic.gdx.Gdx.input;

public class My3DCompass implements ApplicationListener, InputProcessor {

	private Environment environment;
	private PerspectiveCamera cam;
	private CameraInputController camController;
	private ModelBatch modelBatch;
	private Model xAxisArrow, yAxisArrow, zAxisArrow, compassModel, gravityModel;
	private float[] viewMatrix;

	enum ModelKey {
		XAXIS,
		YAXIS,
		ZAXIS,
		COMPASS,
		GRAVITY
	}

	private final HashMap<ModelKey, ModelInstance> modelInstances =
	new HashMap<ModelKey, ModelInstance>();

	@Override
	public void create () {
		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

		modelBatch = new ModelBatch();

		cam = new PerspectiveCamera(65, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0.0f,0.0f,20.0f);
		cam.near = 1f;
		cam.far = 300f;
		cam.update();
		camController = new CameraInputController(cam);
		Gdx.input.setInputProcessor(camController);

		ModelBuilder modelBuilder = new ModelBuilder();

		xAxisArrow = modelBuilder.createArrow(0f,0f,0f,-10f,0f,0f,0.1f,0.1f,5,
				GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.GREEN)),
				Usage.Position | Usage.Normal);

		yAxisArrow = modelBuilder.createArrow(0f,0f,0f,0f,10f,0f,0.1f,0.1f,5,
				GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.BLUE)),
				Usage.Position | Usage.Normal);

		zAxisArrow = modelBuilder.createArrow(0f,0f,0f,0f,0f,-10f,0.1f,0.1f,5,
				GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.RED)),
				Usage.Position | Usage.Normal);

		compassModel = modelBuilder.createArrow(0f,0f,0f,0f,10f,0f,0.3f,0.3f,5,
				GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.YELLOW)),
				Usage.Position | Usage.Normal);

		gravityModel = modelBuilder.createArrow(0f,0f,0f,0f,0f,-10f,0.3f,0.3f,5,
				GL20.GL_TRIANGLES, new Material(ColorAttribute.createDiffuse(Color.CYAN)),
				Usage.Position | Usage.Normal);

		modelInstances.put(ModelKey.XAXIS, new ModelInstance(xAxisArrow));
		modelInstances.put(ModelKey.YAXIS, new ModelInstance(yAxisArrow));
		modelInstances.put(ModelKey.ZAXIS, new ModelInstance(zAxisArrow));
		modelInstances.put(ModelKey.COMPASS, new ModelInstance(compassModel));
		modelInstances.put(ModelKey.GRAVITY, new ModelInstance(gravityModel));

//		modelInstances.get(ModelKey.XAXIS).transform.rotate(Vector3.X,0);
//		modelInstances.get(ModelKey.YAXIS).transform.rotate(Vector3.X,45);
//		modelInstances.get(ModelKey.ZAXIS).transform.rotate(Vector3.X,90);
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		float x = cam.direction.x;
		float y = cam.direction.y;
		float z = cam.direction.z;
		camController.update();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		if(input.isPeripheralAvailable(Input.Peripheral.Compass)){
			modelInstances.get(ModelKey.COMPASS).transform.setFromEulerAngles(
					Gdx.input.getPitch(),
					Gdx.input.getRoll(),
					Gdx.input.getAzimuth()+90);
			modelInstances.get(ModelKey.GRAVITY).transform.setFromEulerAngles(
					Gdx.input.getPitch(),
					Gdx.input.getRoll(),
					Gdx.input.getAzimuth());

	}
		modelBatch.begin(cam);
		modelBatch.render(modelInstances.values(), environment);
		modelBatch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose () {

	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}


	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
