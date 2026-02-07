import os
import json

from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import anthropic

app = FastAPI(title="SmartFixer API")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


class DiagnoseRequest(BaseModel):
    description: str
    image_url: str | None = None


class DiagnoseResponse(BaseModel):
    title: str
    difficulty: str
    estimated_time: str
    tools: list[str]
    steps: list[str]


SYSTEM_PROMPT = """\
You are a home repair diagnostic assistant. Given a description of a home repair issue, \
provide a structured diagnosis. You MUST respond with valid JSON only, no other text. \
Use this exact format:
{
  "title": "Short title of the issue",
  "difficulty": "Easy|Medium|Hard",
  "estimated_time": "e.g. 30-45 minutes",
  "tools": ["tool1", "tool2"],
  "steps": ["Step 1 description", "Step 2 description"]
}
Provide practical, safe, actionable advice. Include 4-8 steps and 2-6 tools as appropriate.\
"""


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.post("/diagnose", response_model=DiagnoseResponse)
async def diagnose(request: DiagnoseRequest):
    api_key = os.environ.get("ANTHROPIC_API_KEY")
    if not api_key:
        raise HTTPException(status_code=500, detail="ANTHROPIC_API_KEY not configured")

    client = anthropic.Anthropic(api_key=api_key)

    user_message = f"Diagnose this home repair issue: {request.description}"
    if request.image_url:
        user_message += f"\n\nImage URL: {request.image_url}"

    try:
        message = client.messages.create(
            model="claude-sonnet-4-5-20250929",
            max_tokens=1024,
            system=SYSTEM_PROMPT,
            messages=[{"role": "user", "content": user_message}],
        )

        response_text = message.content[0].text
        result = json.loads(response_text)

        return DiagnoseResponse(**result)
    except json.JSONDecodeError:
        raise HTTPException(status_code=502, detail="Failed to parse AI response")
    except anthropic.APIError as e:
        raise HTTPException(status_code=502, detail=f"AI service error: {e}")
